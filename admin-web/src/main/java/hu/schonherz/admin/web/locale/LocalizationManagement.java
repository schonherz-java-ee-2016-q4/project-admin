package hu.schonherz.admin.web.locale;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LocalizationManagement {

    private static final ObjectProperty<Locale> LOCALE_PROPERTY;
    private static final ObjectProperty<ResourceBundle> MESSAGE_PROPERTY;

    static {
        LOCALE_PROPERTY = new SimpleObjectProperty<>();
        MESSAGE_PROPERTY = new SimpleObjectProperty<>();

        ChangeListener<Locale> listener = (ObservableValue<? extends Locale> ov, Locale oldLocale, Locale newLocale) -> {
            refreshLocaleMessages(newLocale);
        };
        LOCALE_PROPERTY.addListener(listener);

        LOCALE_PROPERTY.set(Locale.getDefault());
    }

    private LocalizationManagement() {
    }

    public static Locale getLocale() {
        return LOCALE_PROPERTY.get();
    }

    public static String getLanguage() {
        return LOCALE_PROPERTY.get().getLanguage();
    }

    public static boolean setLanguage(@NonNull final String language) {
        if (!language.equals(LOCALE_PROPERTY.get().getLanguage())) {
            Locale newLocale = new Locale(language);
            LOCALE_PROPERTY.set(newLocale);
            return true;
        }

        return false;
    }

    public static ObjectProperty<ResourceBundle> getMessageProperty() {
        return MESSAGE_PROPERTY;
    }

    private static void refreshLocaleMessages(final Locale newLocale) {
        try {
            ResourceBundle localMessages = ResourceBundle.getBundle("i18n.localization", newLocale);
            MESSAGE_PROPERTY.set(localMessages);
        } catch (Exception e) {
            String message = "Could not create resource bundle for localization messages!";
            log.error(message, e);
            throw new IllegalStateException(message, e);
        }

    }

}
