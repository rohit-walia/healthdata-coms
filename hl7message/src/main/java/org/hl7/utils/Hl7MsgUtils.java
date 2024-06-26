package org.hl7.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.jacksonhelper.deserialize.DeserializeToObj;
import org.github.jacksonhelper.serialize.SerializeToStr;
import org.hl7.Hl7Message;
import org.hl7.segment.TQ1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * All helper functions related to hl7 message can live here.
 */
public final class Hl7MsgUtils {

  public static Hl7Message copy(Hl7Message origMsg) {
    return DeserializeToObj.fromString(SerializeToStr.toString(origMsg), Hl7Message.class);
  }

  public static Optional<String> getField(String[] hl7MsgField, int index) {
    return ArrayUtils.isArrayIndexValid(hl7MsgField, index) ? Optional.of(hl7MsgField[index]) : Optional.empty();
  }

  /**
   * Determines if given hl7 message has multiple schedules.
   */
  public static boolean isMultiSchedule(Hl7Message hl7Message) {
    var schedules = hl7Message.getTq1();
    if (schedules == null || schedules.isEmpty()) {
      throw new IllegalArgumentException("TQ1 segment(s) of Hl7 message cant be null or empty.");
    }
    return schedules.size() > 1;
  }

  /**
   * Concatenates multiple schedule instructions.
   */
  public static String joinMultiSchedulesWithDelimiter(List<TQ1> schedules, String delimiter) {
    if (schedules == null || schedules.isEmpty()) {
      throw new IllegalArgumentException("Hl7Message or its Tq1 segment is null");
    }

    if (schedules.size() < 2) {
      throw new IllegalArgumentException("This function applies only to multi-schedule orders");
    }

    return schedules.stream()
        .map(TQ1::getTq1_11_admin_instructions)
        .collect(Collectors.joining(delimiter));
  }

  /**
   * Convenient way to get the instructions for single schedule message.
   */
  public static String getInstructionsForSingleSchedule(Hl7Message hl7Message) {
    var schedules = hl7Message.getTq1();

    if (schedules == null || schedules.isEmpty()) {
      throw new IllegalArgumentException("TQ1 segment(s) of Hl7 message cant be null or empty.");
    }

    // single schedule orders SHOULD have instructions in TQ1 segment. But not enforced.
    if (StringUtils.isNotBlank(schedules.get(0).getTq1_11_admin_instructions())) {
      return hl7Message.getTq1().get(0).getTq1_11_admin_instructions();
    }

    // fallback to RXE segment if TQ1 segment is empty.
    return hl7Message.getRxe().getRxe_7_adminInstructionsText();
  }

  /**
   * Convenient way to update given hl7 message schedule start and end dates.
   */
  public static void updateMessageStartAndEndDate(Hl7Message hl7Message, LocalDateTime startDate, LocalDateTime endDate) {
    TQ1 tq1 = hl7Message.getTq1().get(0);

    if (startDate != null) {
      tq1.setTq1_7_startDateTime(Hl7DateUtils.formatToHl7(startDate));
    }
    if (endDate != null) {
      tq1.setTq1_8_endDateTime(Hl7DateUtils.formatToHl7(endDate));
    }
  }

  /**
   * Convenient way to update message admin times.
   */
  public static void updateMessageAdminTime(Hl7Message hl7Message, String adminTime) {
    TQ1 tq1 = hl7Message.getTq1().get(0);

    if (adminTime != null) {
      tq1.setTq1_4_explicit_time(adminTime);
    }
  }

  /**
   * The drug name is free text and can be found in many places on UI. Sometimes it's the only way to identify the order
   * from UI. Appending a unique id to drug name will help in identifying the order.
   */
  public static void appendUniqueIdToDrugName(Hl7Message hl7Message) {
    String origDrugName = hl7Message.getRxe().getRxe_2_2_drugName();
    hl7Message.getRxe().setRxe_2_2_drugName(origDrugName + " " + RandomStringUtils.randomAlphanumeric(3));
  }

  /**
   * The order id is used to uniquely identify an order. Scrambling this field in message will force new order creation
   * and processing existing orders.
   */
  public static void scrambleOrderId(Hl7Message hl7Message) {
    hl7Message.getOrc().setOrc_3_fillerOrderNumber(RandomStringUtils.randomNumeric(5));
  }

  /**
   * Gets the HL7 String from values for a Composite Field.
   */
  public static String getCompositeFieldString(String... values) {
    List<String> valuesList = new ArrayList<>(Arrays.asList(values));
    while (!valuesList.isEmpty()) {
      if (StringUtils.isBlank(valuesList.get(valuesList.size() - 1))) {
        valuesList.remove(valuesList.size() - 1);
      } else {
        break;
      }
    }

    return valuesList.stream()
        .reduce((a, b) -> (a == null ? "" : a) + "^" + (b == null ? "" : b))
        .orElse("");
  }
}
