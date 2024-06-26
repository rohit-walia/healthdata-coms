package org.hl7.utils;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class Hl7DateUtils {
  public static final DateTimeFormatter HL7_SPEC_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  public static final DateTimeFormatter M_d_yyyy = DateTimeFormatter.ofPattern("M/d/yyyy");
  public static final DateTimeFormatter M_d_yyyy_HHmm = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm");
  public static final DateTimeFormatter MMM_dd_yyyy = DateTimeFormatter.ofPattern("MMM dd, yyyy");
  public static final DateTimeFormatter MM_dd_yyyy_HHmm = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

  public static String formatToHl7(@NonNull LocalDateTime date) {
    return formatLocalDateTime(date, HL7_SPEC_DATE_TIME_FORMAT);
  }

  public static String formatToHl7(@NonNull LocalTime time) {
    return time.format(DateTimeFormatter.ofPattern("HHmm"));
  }

  public static String formatLocalDateTime(@NonNull LocalDateTime date, DateTimeFormatter formatter) {
    return date.format(formatter);
  }

  public static String formatFromHl7(@NonNull String hl7DateTimeValue, DateTimeFormatter formatter) {
    return LocalDateTime.parse(hl7DateTimeValue, HL7_SPEC_DATE_TIME_FORMAT).format(formatter);
  }

  public static OffsetDateTime formatFromHl7ToOffsetDateTime(@NonNull String hl7DateTimeValue) {
    LocalDateTime localDateTIme = LocalDateTime.parse(hl7DateTimeValue, HL7_SPEC_DATE_TIME_FORMAT);
    return OffsetDateTime.of(localDateTIme, ZoneId.systemDefault().getRules().getOffset(localDateTIme));
  }
}
