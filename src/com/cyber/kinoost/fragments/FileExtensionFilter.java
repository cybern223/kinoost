package com.cyber.kinoost.fragments;

import java.io.File;
import java.io.FilenameFilter;

class FileExtensionFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3") || name.endsWith(".MP3"));
    }

}