package scims.ui.swing;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeTextField extends JTextField {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final String allowedCharacters = "0123456789:AMP";

    private String _placeholder;
    private boolean _paintingOnFocus;
    private Color _placeholderColor;
    public DateTimeTextField() {
        ((javax.swing.text.AbstractDocument) getDocument()).setDocumentFilter(new DateTimeDocumentFilter());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");
        String formattedNow = now.format(formatter);
        setPlaceholder(formattedNow);
        setPlaceholderColor(Color.GRAY);
        setPaintingOnFocus(true);
    }

    public String getPlaceholder() {
        return _placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        _placeholder = placeholder;
        repaint();
    }

    public boolean isPaintingOnFocus() {
        return _paintingOnFocus;
    }

    public void setPaintingOnFocus(final boolean paintingOnFocus) {
        _paintingOnFocus = paintingOnFocus;
        repaint();
    }

    public Color getPlaceholderColor() {
        return _placeholderColor;
    }

    public void setPlaceholderColor(final Color placeholderColor) {
        _placeholderColor = placeholderColor;
        repaint();
    }


    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (getPlaceholder() != null && getText().isEmpty() && (isPaintingOnFocus() || !isFocusOwner())) {
            try {
                final Rectangle rect = getUI().modelToView(this, 0);
                final Insets insets = getInsets();
                g.setFont(getFont());
                g.setColor(getPlaceholderColor() == null ? getForeground() : getPlaceholderColor());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.drawString(getPlaceholder(), rect.x, getHeight() - insets.top - insets.bottom - rect.y);
            } catch (final BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class RepaintFocusListener implements FocusListener {
        @Override
        public void focusGained(final FocusEvent e) {
            repaint();
        }

        @Override
        public void focusLost(final FocusEvent e) {
            repaint();
        }
    }

    private class DateTimeDocumentFilter extends DocumentFilter {
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String beforeText = currentText.substring(0, offset);
            String afterText = currentText.substring(offset + length);

            // Only allow the user to enter allowed characters
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (allowedCharacters.indexOf(c) != -1) {
                    sb.append(c);
                }
            }
            text = sb.toString();

            String newText = beforeText + text + afterText;
            try {
                if(newText.contains("-"))
                {
                    char ch = '-';
                    long count = text.chars().filter(c -> c == ch).count();
                    if(count > 2) {
                        throw new IOException("Invalid date-time");
                    }
                    String[] split = newText.split("-");
                    if(split.length == 1) {
                        if(!split[0].matches("\\d{2}") || Integer.parseInt(split[0]) > 12) {
                            throw new IOException("Invalid date-time");
                        }
                    }
                    else if(split.length == 2) {
                        if(!split[0].matches("\\d{2}") || Integer.parseInt(split[0]) > 12) {
                            throw new IOException("Invalid date-time");
                        }
                        if(split[1].length() > 2 || Integer.parseInt(split[1]) > 31) {
                            throw new IOException("Invalid date-time");
                        }
                        if(newText.length() == 5) {
                            text += "-";
                        }
                    }
                    else if(split.length == 3 && !newText.contains(" ")) {
                        if(text.equalsIgnoreCase(":")) {
                            throw new IOException("Invalid date time");
                        }
                        if(!split[0].matches("\\d{2}")
                                ||!split[1].matches("\\d{2}") || Integer.parseInt(split[1]) > 31) {
                            throw new IOException("Invalid date-time");
                        }
                        if(split[2].length() > 4) {
                            throw new IOException("Invalid date-time");
                        }
                        if(newText.length() == 10) {
                            text += " ";
                        }
                    } else {
                        String[] spaceSplit = newText.split(" ");
                        if(spaceSplit.length == 2) {
                            if(spaceSplit[1].contains(":")) {
                                char colonChar = ':';
                                count = newText.chars().filter(c -> c == colonChar).count();
                                String[] colonSplit = spaceSplit[1].split(":");
                                if(count > 1 || colonSplit.length == 0) {
                                    throw new IOException("Invalid date-time");
                                }

                                if(colonSplit.length > 1){
                                    if(Integer.parseInt(colonSplit[1]) > 59) {
                                        throw new IOException("Invalid time");
                                    }
                                }
                                if(colonSplit.length > 2){
                                    throw new IOException("Invalid time");
                                }
                                if(colonSplit.length > 1 && colonSplit[1].length() == 2) {
                                    String morningEvening = "PM";
                                    if(Integer.parseInt(colonSplit[0]) < 12 && Integer.parseInt(colonSplit[0]) >= 7) {
                                        morningEvening = "AM";
                                    }
                                    text += " " + morningEvening;
                                }
                            }
                            else {
                                if(Integer.parseInt(spaceSplit[1]) > 12) {
                                    throw new IOException("Invalid time");
                                }
                                if(spaceSplit[1].length() == 2) {
                                    text += ":";
                                }
                            }
                        }
                        else if (spaceSplit.length > 2) {
                            if(spaceSplit[2].length() > 2) {
                                throw new IOException("Invalid AM/PM");
                            }
                            if(spaceSplit[2].length() == 2 && !spaceSplit[2].equalsIgnoreCase("AM") && !spaceSplit[2].equalsIgnoreCase("PM")) {
                                throw new IOException("Invalid AM/PM");
                            }
                            if(spaceSplit[2].length() == 1 && text.equalsIgnoreCase("M")) {
                                throw new IOException("Invalid AM/PM");
                            }
                            if(spaceSplit[2].length() == 1) {
                                text += "M";
                            }
                        }
                    }
                } else if(newText.length() > 2 || Integer.parseInt(newText) > 12)
                {
                    throw new IOException("Invalid date-time");
                }
                else if(newText.length() == 2) {
                    text += "-";
                }
                super.replace(fb, offset, length, text, attrs);
            }catch (IOException ignored){

            }
        }
    }

    public ZonedDateTime getZonedDateTime() throws DateTimeParseException {
        String text = getText();
        ZonedDateTime retVal = null;
        if(text != null) {
            text = text.trim();
            try {
                retVal = ZonedDateTime.parse(text, DATE_TIME_FORMATTER.withZone(java.time.ZoneId.systemDefault()));
            } catch (DateTimeParseException e) {
                LocalDate localDate = LocalDate.parse(text, DATE_FORMATTER);
                ZoneId zoneId = ZoneId.systemDefault();
                retVal = ZonedDateTime.of(localDate, LocalTime.MIN, zoneId);
            }
        }

        return retVal;
    }
}