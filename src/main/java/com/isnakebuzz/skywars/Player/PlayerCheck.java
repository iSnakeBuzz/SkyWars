package com.isnakebuzz.skywars.Player;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class PlayerCheck {

    private String licenseKey;
    private Plugin plugin;
    private String validationServer;
    private LogType logType = LogType.NORMAL;
    private String securityKey = "7da89b5b-78d0-400e-9146-1aa7e9af6e46";
    private boolean debug = false;

    public PlayerCheck(String licenseKey, String validationServer, Plugin plugin) {
        this.licenseKey = licenseKey;
        this.plugin = plugin;
        this.validationServer = validationServer;
    }

    private static String xor(String s1, String s2) {
        String s0 = "";
        for (int i = 0; i < (s1.length() < s2.length() ? s1.length() : s2.length()); i++)
            s0 += Byte.valueOf("" + s1.charAt(i)) ^ Byte.valueOf("" + s2.charAt(i));
        return s0;
    }

    public PlayerCheck setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
        return this;
    }

    public PlayerCheck setConsoleLog(LogType logType) {
        this.logType = logType;
        return this;
    }

    public PlayerCheck debug() {
        debug = true;
        return this;
    }

    public boolean register() {
        //log(0, "Connecting to License-Server...");
        ValidationType vt = isValid();
        if (vt == ValidationType.VALID) {
            //log(1, "License valid!");
            return true;
        } else {
            log(1, "Please check your" + plugin.getName() + " version!");

            Bukkit.getScheduler().cancelTasks(plugin);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    public boolean isValidSimple() {
        return (isValid() == ValidationType.VALID);
    }


    //
    // Cryptographic
    //

    public ValidationType isValid() {
        String rand = toBinary(UUID.randomUUID().toString());
        String sKey = toBinary(securityKey);
        String key = toBinary(licenseKey);

        try {
            URL url = new URL(validationServer + "?v1=" + xor(rand, sKey) + "&v2=" + xor(rand, key) + "&pl=" + plugin.getName());
            if (debug) System.out.println("RequestURL -> " + url.toString());
            Scanner s = new Scanner(url.openStream());
            if (s.hasNext()) {
                String response = s.next();
                s.close();
                try {
                    return ValidationType.valueOf(response);
                } catch (IllegalArgumentException exc) {
                    String respRand = xor(xor(response, key), sKey);
                    if (rand.substring(0, respRand.length()).equals(respRand)) return ValidationType.VALID;
                    else return ValidationType.WRONG_RESPONSE;
                }
            } else {
                s.close();
                return ValidationType.PAGE_ERROR;
            }
        } catch (IOException exc) {
            if (debug) exc.printStackTrace();
            return ValidationType.URL_ERROR;
        }
    }

    //
    // Enums
    //

    private String toBinary(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    private void log(int type, String message) {
        if (logType == LogType.NONE || (logType == LogType.LOW && type == 0)) return;
        System.out.println(message);
    }

    //
    // Binary methods
    //

    public enum LogType {
        NORMAL, LOW, NONE;
    }

    //
    // Console-Log
    //

    public static enum ValidationType {
        WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
    }
}
