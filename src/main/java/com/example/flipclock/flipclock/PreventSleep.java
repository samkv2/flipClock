package com.example.flipclock.flipclock;

public class PreventSleep {

    public static void activate() {
        Thread keepAwakeThread = new Thread(() -> {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    Runtime.getRuntime().exec("powercfg /change standby-timeout-ac 0");
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("caffeinate");
                } else if (os.contains("nix") || os.contains("nux")) {
                    while (true) {
                        Runtime.getRuntime().exec("xdotool key shift");
                        Thread.sleep(30000);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to prevent sleep: " + e.getMessage());
            }
        });

        keepAwakeThread.setDaemon(true);
        keepAwakeThread.start();
    }
}
