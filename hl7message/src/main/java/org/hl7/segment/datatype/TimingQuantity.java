package org.hl7.segment.datatype;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.hl7.utils.Hl7MsgUtils;

import java.util.stream.Stream;

/**
 * This data type, called 'Timing Quantity' describes when a service should be performed and how frequently.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/DataTypes/TQ">Standard hl7 v2.5 spec - Data type TQ</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class TimingQuantity implements IComponent {
  @Builder.Default
  private String quantity = "";
  @Builder.Default
  private String interval = "";
  @Builder.Default
  private String duration = "";
  @Builder.Default
  private String startDateTime = "";
  @Builder.Default
  private String endDateTime = "";
  @Builder.Default
  private String priority = "";

  @Override
  public String print() {
    return Hl7MsgUtils.getCompositeFieldString(quantity, interval, duration, startDateTime, endDateTime, priority);
  }

  @Override
  public boolean isEmpty() {
    return Stream.of(quantity, interval, duration, startDateTime, endDateTime, priority).allMatch(String::isBlank);
  }

  /**
   * Converts String to TimingQuantity object.
   */
  public static TimingQuantity fromString(String component) {
    String[] fields = component.split("\\".concat(CARET), -1);
    TimingQuantityBuilder timingQuantity = TimingQuantity.builder();
    getField(fields, 0).ifPresent(timingQuantity::quantity);
    getField(fields, 1).ifPresent(timingQuantity::interval);
    getField(fields, 2).ifPresent(timingQuantity::duration);
    getField(fields, 3).ifPresent(timingQuantity::startDateTime);
    getField(fields, 4).ifPresent(timingQuantity::endDateTime);
    getField(fields, 5).ifPresent(timingQuantity::priority);
    return timingQuantity.build();
  }
}
