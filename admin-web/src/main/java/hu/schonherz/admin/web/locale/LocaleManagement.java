package hu.schonherz.admin.web.locale;

import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import lombok.NonNull;

public final class LocaleManagement {

    private static final ObjectProperty<Locale> LOCALE_PROPERTY;

    static {
        LOCALE_PROPERTY = new SimpleObjectProperty<>(Locale.getDefault());
    }

    private LocaleManagement() {
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

    public static void addListener(@NonNull final ChangeListener<? super Locale> listener) {
        LOCALE_PROPERTY.addListener(listener);
    }

}
