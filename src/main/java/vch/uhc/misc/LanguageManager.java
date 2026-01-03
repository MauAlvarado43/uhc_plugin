package vch.uhc.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;

import vch.uhc.UHC;

/**
 * Handles localization and message loading from properties files.
 */
public class LanguageManager {

    private Properties messages;
    private String currentLanguage;
    private final File dataFolder;

    public LanguageManager() {
        this.dataFolder = UHC.getPlugin().getDataFolder();
        this.currentLanguage = "en";
        initializeLanguage(currentLanguage);
    }

    private void initializeLanguage(String languageCode) {
        messages = new Properties();
        this.currentLanguage = languageCode;

        loadLanguage(languageCode);
    }

    /**
     * Loads a specific language file from either the data folder or internal
     * resources.
     *
     * @param languageCode The locale code (e.g. 'en', 'es').
     */
    public void loadLanguage(String languageCode) {
        messages = new Properties();
        this.currentLanguage = languageCode;

        File langFile = new File(dataFolder, "lang/messages_" + languageCode + ".properties");

        if (langFile.exists()) {
            try (FileInputStream fis = new FileInputStream(langFile); InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                messages.load(reader);
                UHC.getPlugin().getLogger().info(() -> "Loaded language file: " + langFile.getName());
                return;
            } catch (IOException e) {
                UHC.getPlugin().getLogger().log(Level.WARNING, "Error loading language file from data folder", e);
            }
        }

        try (InputStream is = UHC.getPlugin().getResource("lang/messages_" + languageCode + ".properties")) {
            if (is != null) {
                try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    messages.load(reader);
                    UHC.getPlugin().getLogger().info(() -> "Loaded language from resources: messages_" + languageCode + ".properties");
                }
            } else {
                UHC.getPlugin().getLogger().warning(() -> "Language file not found: messages_" + languageCode + ".properties");
                if (!languageCode.equals("en")) {
                    loadLanguage("en");
                }
            }
        } catch (IOException e) {
            UHC.getPlugin().getLogger().log(Level.SEVERE, "Error loading language from resources", e);
        }
    }

    /**
     * Retrieves a translated and formatted message for a specific key.
     *
     * @param key The property key in the language file.
     * @param args Optional arguments for MessageFormat formatting.
     * @return The formatted message string.
     */
    public String getMessage(String key, Object... args) {
        String message = messages.getProperty(key);

        if (message == null) {
            UHC.getPlugin().getLogger().warning(() -> "Missing translation for key: " + key);
            return "§c[Missing: " + key + "]";
        }

        message = message.replace("§", "§");

        if (args.length > 0) {
            try {
                return MessageFormat.format(message, args);
            } catch (IllegalArgumentException e) {
                UHC.getPlugin().getLogger().log(Level.WARNING, "Error formatting message: " + key, e);
                return message;
            }
        }

        return message;
    }

    /**
     * Returns the currently active language code.
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Changes the current language and reloads messages.
     */
    public void setLanguage(String languageCode) {
        loadLanguage(languageCode);
    }

    /**
     * Get available languages by scanning the lang folder
     */
    public String[] getAvailableLanguages() {
        File langFolder = new File(dataFolder, "lang");
        if (!langFolder.exists()) {
            return new String[]{"en", "es"};
        }

        File[] files = langFolder.listFiles((dir, name)
                -> name.startsWith("messages_") && name.endsWith(".properties"));

        if (files == null || files.length == 0) {
            return new String[]{"en", "es"};
        }

        String[] languages = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            languages[i] = filename.substring(9, filename.length() - 11);
        }

        return languages;
    }
}
