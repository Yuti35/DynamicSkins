package com.yuti.dynamicskins.common.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Utils {

    public static void sendErrorMessageToEntity(ICommandSender entity, String message, Object...args) {
        TextComponentTranslation comp = new TextComponentTranslation(message, args);
        comp.getStyle().setColor(TextFormatting.RED);
        entity.sendMessage(comp);
    }

    public static void sendValidMessageToEntity(ICommandSender entity, String message, Object...args) {
        TextComponentTranslation comp = new TextComponentTranslation(message, args);
        comp.getStyle().setColor(TextFormatting.GREEN);
        entity.sendMessage(comp);
    }

    public static void sendInfoMessageToEntity(ICommandSender entity, String message, Object...args) {
        TextComponentTranslation comp = new TextComponentTranslation(message, args);
        comp.getStyle().setColor(TextFormatting.YELLOW);
        entity.sendMessage(comp);
    }

    public static void sendMessageToEntity(ICommandSender entity, String message, Object...args) {
        TextComponentTranslation comp = new TextComponentTranslation(message, args);
        entity.sendMessage(comp);
    }

    public static void sendMessageURLToEntity(ICommandSender entity, String url) {
        if(url != null) {
            TextComponentString comp = new TextComponentString(url);
            comp.getStyle().setColor(TextFormatting.BLUE);
            comp.getStyle().setUnderlined(true);
            try {
                new URL(url); //Allows to test if the URL is "valid"
                ClickEvent clickMusic = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
                comp.getStyle().setClickEvent(clickMusic);
            } catch (MalformedURLException e) {
                //Do nothing (= not adding open url action)
            }
            entity.sendMessage(comp);
        }
    }

    public static String getHostName(String url) {
        URI uri;
        try {
            uri = new URI(url);
            String hostname = uri.getHost();
            if (hostname != null) {
                return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
            }
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
