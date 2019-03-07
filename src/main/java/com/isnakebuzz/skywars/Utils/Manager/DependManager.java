package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Calls.Callback;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DependManager {

    private Main plugin;

    public DependManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadDepends() {
        try {
            this.downloadDependency(new URL("http://central.maven.org/maven2/com/zaxxer/HikariCP/3.3.1/HikariCP-3.3.1.jar"), "HikariCP");
            this.downloadDependency(new URL("http://central.maven.org/maven2/org/slf4j/slf4j-api/1.7.26/slf4j-api-1.7.26.jar"), "slf4j-api");
            this.downloadDependency(new URL("http://central.maven.org/maven2/com/flowpowered/flow-nbt/1.0.0/flow-nbt-1.0.0.jar"), "jnbt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void downloadDepends2(URL url, File fileName, Callback<Double> callback) {
        BufferedInputStream in = null;
        FileOutputStream out = null;

        try {
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();

            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(fileName);
            byte data[] = new byte[1024];
            int count;
            double sumCount = 0.0;

            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);

                sumCount += count;
                if (size > 0) {
                    double porcentage = (sumCount / size * 100.0);
                    callback.done(porcentage);
                }
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
        }
    }

    private void downloadDependency(URL url, String name) {
        File libraries = new File(plugin.getDataFolder(), "Libraries");
        if (!libraries.exists()) libraries.mkdir();

        File fileName = new File(libraries, name + ".jar");

        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
                downloadDepends2(url, fileName, value -> {
                    plugin.log("DependsManager", "Downloading " + name + " " + String.format("%.1f", value).replaceAll(",", ".") + "%");
                    if (value >= 100) {
                        loadJarFile(fileName);
                        plugin.log("DependsManager", "Download of " + name + " has been completed.");
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            loadJarFile(fileName);
        }

    }

    private void download(final URL url, final File destinyFile) throws IOException {
        InputStream stream = url.openStream();
        ReadableByteChannel rbc = Channels.newChannel(stream);
        FileOutputStream fos = new FileOutputStream(destinyFile);
        fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
        fos.flush();
        fos.close();
    }

    private void loadJarFile(File jar) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Class<?> getClass = classLoader.getClass();
            Method method = getClass.getSuperclass().getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, jar.toURI().toURL());
        } catch (NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
    }

}
