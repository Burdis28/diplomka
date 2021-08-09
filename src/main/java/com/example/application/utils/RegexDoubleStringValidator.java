package com.example.application.utils;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDoubleStringValidator extends AbstractValidator<BigDecimal> {

    private final Pattern pattern;
    private final boolean complete;
    private transient Matcher matcher = null;

    /**
     * Constructs a validator with the given error message. The substring "{0}"
     * is replaced by the value that failed validation.
     *
     * @param errorMessage the message to be included in a failed result, not null
     */
    public RegexDoubleStringValidator(String errorMessage, String regexp) {
        this(errorMessage, regexp, true);
    }

    /**
     * Creates a validator for checking that the regular expression matches the
     * string to validate.
     *
     * @param errorMessage
     *            the message to display in case the value does not validate.
     * @param regexp
     *            a Java regular expression
     * @param complete
     *            true to use check for a complete match, false to look for a
     *            matching substring
     *
     */
    public RegexDoubleStringValidator(String errorMessage, String regexp,
                           boolean complete) {
        super(errorMessage);
        pattern = Pattern.compile(regexp);
        this.complete = complete;
    }

    @Override
    public ValidationResult apply(BigDecimal value, ValueContext context) {
        return toResult(value, isValid(value.toString()));
    }

    protected boolean isValid(String value) {
        if (value == null) {
            return true;
        }
        if (complete) {
            return getMatcher(value).matches();
        } else {
            return getMatcher(value).find();
        }
    }

    private Matcher getMatcher(String value) {
        if (matcher == null) {
            matcher = pattern.matcher(value);
        } else {
            matcher.reset(value);
        }
        return matcher;
    }
}
