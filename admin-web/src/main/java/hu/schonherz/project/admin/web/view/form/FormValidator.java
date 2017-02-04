package hu.schonherz.project.admin.web.view.form;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.validator.routines.EmailValidator;

public class FormValidator {

    // Field length boundaries
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_FIELD_LENGTH = 15;
    // Error messages
    public static final String EMPTY_FIELD = "Field must be filled";
    public static final String TOO_LONG_FIELD = "This field must not be longer than " + MAX_FIELD_LENGTH + " characters";
    public static final String SHORT_PASSWORD = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long";
    public static final String INVALID_EMAIL = "Invalid e-mail";
    public static final String CONFIRM_PASSWORD = "Password and Confirm password fields must be equal";

    @ToString
    public static class MessageBinding {

        private final Map<MessageType, String> binding;

        public enum MessageType {
            EMAIL, USERNAME, PASSWORD, NEW_PASSWORD
        }

        public MessageBinding() {
            binding = new HashMap<>();
        }

        public void addMessage(@NonNull MessageType type, @NonNull String message) {
            binding.put(type, message);
        }

        public String getMessage(@NonNull MessageType type) {
            return binding.get(type);
        }

        public boolean hasMessageOfType(@NonNull MessageType type) {
            return binding.containsKey(type);
        }

        public void removeMessageOfType(@NonNull MessageType type) {
            binding.remove(type);
        }

        public boolean isEmpty() {
            return binding.isEmpty();
        }
    }

    private FormValidator() {
    }

    public static MessageBinding validateProfileForm(@NonNull ProfileForm form) {
        MessageBinding binding = new MessageBinding();
        // Validate basic user data
        validateUserForm(form, binding);
        if (!binding.isEmpty()) {
            return binding;
        }

        // Validate new password, if the user gave one
        String newPassword = form.getNewPassword();
        String confirmNewPassword = form.getConfirmNewPassword();
        if (newPassword != null && !(newPassword.isEmpty())) {
            if (newPassword.length() < MIN_PASSWORD_LENGTH) {
                binding.addMessage(MessageBinding.MessageType.NEW_PASSWORD, SHORT_PASSWORD);
            }
            if (newPassword.length() > MAX_FIELD_LENGTH) {
                binding.addMessage(MessageBinding.MessageType.NEW_PASSWORD, TOO_LONG_FIELD);
            }

            if (!newPassword.equals(confirmNewPassword)) {
                binding.addMessage(MessageBinding.MessageType.NEW_PASSWORD, CONFIRM_PASSWORD);
            }
        }

        return binding;
    }

    public static MessageBinding validateRegistrationForm(@NonNull RegistrationForm form) {
        MessageBinding binding = new MessageBinding();
        // Validate basic user data
        validateUserForm(form, binding);
        if (!binding.isEmpty()) {
            return binding;
        }

        // Validate the confirm password field
        if (!(form.getConfirmPassword() != null && !form.getConfirmPassword().isEmpty())
                || !form.getPassword().equals(form.getConfirmPassword())) {
            binding.addMessage(MessageBinding.MessageType.PASSWORD, CONFIRM_PASSWORD);
        }

        return binding;
    }

    private static void validateUserForm(UserForm form, MessageBinding binding) {
        // Check if there are empty fields
        emptyFieldCheck(form, binding);
        if (!binding.isEmpty()) {
            return;
        }

        // Check if username or password fields are too long
        tooLongFieldCheck(form, binding);
        if (!binding.isEmpty()) {
            return;
        }

        // Validate password length
        if (form.getPassword().length() < MIN_PASSWORD_LENGTH) {
            binding.addMessage(MessageBinding.MessageType.PASSWORD, SHORT_PASSWORD);
            return;
        }

        // Validate e-mail syntax
        if (!EmailValidator.getInstance().isValid(form.getEmail())) {
            binding.addMessage(MessageBinding.MessageType.EMAIL, INVALID_EMAIL);
        }
    }

    private static void emptyFieldCheck(UserForm form, MessageBinding binding) {
        // Is email null or empty
        if (form.getEmail() == null || form.getEmail().isEmpty()) {
            binding.addMessage(MessageBinding.MessageType.EMAIL, EMPTY_FIELD);
        }
        // Is username null or empty
        if (!(form.getUsername() != null && !form.getUsername().isEmpty())) {
            binding.addMessage(MessageBinding.MessageType.USERNAME, EMPTY_FIELD);
        }
        // Is password null or empty
        if (!(form.getPassword() != null && !form.getPassword().isEmpty())) {
            binding.addMessage(MessageBinding.MessageType.PASSWORD, EMPTY_FIELD);
        }
    }

    private static void tooLongFieldCheck(UserForm form, MessageBinding binding) {
        if (form.getUsername().length() > MAX_FIELD_LENGTH) {
            binding.addMessage(MessageBinding.MessageType.USERNAME, TOO_LONG_FIELD);
        }

        if (form.getPassword().length() > MAX_FIELD_LENGTH) {
            binding.addMessage(MessageBinding.MessageType.PASSWORD, TOO_LONG_FIELD);
        }
    }

}
