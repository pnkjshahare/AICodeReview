package com.ai.codereview.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * GitChangeListener – Automatically detects project switch
 * and correctly watches Git commit changes.
 */
public class GitChangeListener {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean watcherStarted = false;
    private IProject currentProject = null;

    /**
     * Public method to register listener
     */
    public static void registerListener() {
        new GitChangeListener().initialize();
    }

    /**
     * Initialize listener and monitor project changes
     */
    private void initialize() {
        attachToCurrentProject();

        // 🔁 Listen for project changes (import, close, open, switch)
        ResourcesPlugin.getWorkspace().addResourceChangeListener(event -> {
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                IProject newProject = getActiveProject();
                if (newProject != null && !newProject.equals(currentProject)) {
                    ReviewConsole.show("🔁 Project switched → Watching new project: " + newProject.getName());
                    restartWatcher(newProject);
                }
            }
        });
    }

    /**
     * Detect and attach watcher to selected project
     */
    private void attachToCurrentProject() {
        IProject project = getActiveProject();
        if (project == null || !project.isOpen()) {
            ReviewConsole.show("⚠️ Please select a valid Eclipse project before starting AI Review.");
            return;
        }

        if (watcherStarted && project.equals(currentProject)) {
            ReviewConsole.show("ℹ️ Already watching " + project.getName());
            return;
        }

        if (watcherStarted && !project.equals(currentProject)) {
            ReviewConsole.show("🔄 Switching watcher to new project: " + project.getName());
            restartWatcher(project);
            return;
        }

        currentProject = project;
        File gitDir = new File(project.getLocation().toFile(), ".git");
        if (!gitDir.exists()) {
            ReviewConsole.show("⚠️ No .git directory found in: " + project.getName());
            return;
        }

        ReviewConsole.show("📡 Watching Git repo for project: " + project.getName());
        watcherStarted = true;
        startWatcher(gitDir.toPath());
    }

    /**
     * Stop old watcher and reattach
     */
    private void restartWatcher(IProject project) {
        stopWatcher();
        currentProject = project;
        watcherStarted = false;        // Allow fresh start
        executor = Executors.newSingleThreadExecutor(); // New thread
        attachToCurrentProject();
    }

    /**
     * Stop watching
     */
    private void stopWatcher() {
        try {
            executor.shutdownNow();
            ReviewConsole.show("⛔ Stopped watching previous project");
        } catch (Exception e) {
            ReviewConsole.show("⚠️ Failed to stop watcher: " + e.getMessage());
        }
    }

    /**
     * Watch HEAD file for commit updates
     */
    private void startWatcher(Path gitDir) {
        executor.submit(() -> {
            try {
                Path headFile = gitDir.resolve("HEAD");
                if (!Files.exists(headFile)) {
                    ReviewConsole.show("⚠️ HEAD file not found in " + gitDir);
                    return;
                }

                String headContent = Files.readString(headFile).trim();
                Path refFile = headContent.startsWith("ref:")
                        ? gitDir.resolve(headContent.substring(5).trim())
                        : headFile;

                if (!Files.exists(refFile)) {
                    ReviewConsole.show("⚠️ Ref file missing: " + refFile);
                    return;
                }

                WatchService ws = FileSystems.getDefault().newWatchService();
                refFile.getParent().register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
                ReviewConsole.show("🌀 Listening for commit changes at: " + refFile);

                while (true) {
                    WatchKey key = ws.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals(refFile.getFileName().toString())
                                || event.context().toString().endsWith(".lock")) {
                            Thread.sleep(400); // wait for commit to complete
                            handleCommit(gitDir.toFile());
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                ReviewConsole.show("❌ Watcher error: " + e.getMessage());
            }
        });
    }

    /**
     * Handle commit diff
     */
    private void handleCommit(File gitDir) {
        if (!AuthManager.isLoggedIn()) {
            ReviewConsole.show("🔒 Please login to enable AI Code Review.");
            return;
        }

        try {
            Repository repo = new FileRepositoryBuilder()
                    .setGitDir(gitDir)
                    .readEnvironment()
                    .findGitDir()
                    .build();

            try (Git git = new Git(repo)) {
                ObjectId head = repo.resolve("HEAD^{tree}");
                ObjectId prevHead = repo.resolve("HEAD~1^{tree}");

                if (head == null || prevHead == null) {
                    ReviewConsole.show("📭 Not enough commits to compare.");
                    return;
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try (DiffFormatter df = new DiffFormatter(out)) {
                    df.setRepository(repo);
                    df.format(prevHead, head);
                }

                String diff = out.toString(StandardCharsets.UTF_8);
                if (diff.isEmpty()) {
                    ReviewConsole.show("📭 No diff found in commit.");
                    return;
                }

                GitDiffProvider.setLastDiff(diff);
                ReviewConsole.show("📜 Diff captured → sending to AI...");

                String aiResponse = AIClient.sendReview(diff);
                ReviewConsole.show("🤖 Gemini Review Result:\n" + aiResponse);
            }
        } catch (Exception e) {
            ReviewConsole.show("❌ Commit handling failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get currently selected project from Package Explorer
     */
    private IProject getActiveProject() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) return null;

        ISelection selection = window.getSelectionService().getSelection();
        if (!(selection instanceof IStructuredSelection)) return null;

        Object element = ((IStructuredSelection) selection).getFirstElement();
        if (element instanceof IProject) return (IProject) element;
        if (element instanceof IAdaptable) return ((IAdaptable) element).getAdapter(IProject.class);

        return null;
    }
}
