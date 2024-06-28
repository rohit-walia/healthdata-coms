package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import org.hl7.segment.datatype.CodedElement;
import org.hl7.utils.Hl7DateUtils;

import java.time.LocalDateTime;

/**
 * RXD segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/RXD">Standard hl7 v2.5 spec - RXD segment</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class RXD implements ISegment {
  @NonNull
  private String rxd_1_dispenseSubIdCounter;
  @NonNull
  private CodedElement rxd_2_dispenseGiveCode;
  @Builder.Default
  private String rxd_3_dateTimeDispensed = Hl7DateUtils.formatToHl7(LocalDateTime.now());
  @Builder.Default
  private String rxd_4_actualDispenseAmount = EMPTY;
  @Builder.Default
  private String rxd_5_actualDispenseUnit = EMPTY;
  @Builder.Default
  private String rxd_7_prescriptionNumber = EMPTY;

  public static final String segmentId = "RXD";

  @Override
  public String print() {
    return segmentId + "|" + rxd_1_dispenseSubIdCounter + "|" + rxd_2_dispenseGiveCode.print() + "|"
        + rxd_3_dateTimeDispensed + "|" + rxd_4_actualDispenseAmount + "|" + rxd_5_actualDispenseUnit + "||"
        + rxd_7_prescriptionNumber + "||||||||||||||||||||||||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static RXD fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    RXDBuilder rxd = RXD.builder();

    // required fields
    rxd.rxd_1_dispenseSubIdCounter(getField(fields, 1).orElseThrow());

    // optional fields
    getField(fields, 2).map(CodedElement::fromString).ifPresent(rxd::rxd_2_dispenseGiveCode);
    getField(fields, 3).ifPresent(rxd::rxd_3_dateTimeDispensed);
    getField(fields, 4).ifPresent(rxd::rxd_4_actualDispenseAmount);
    getField(fields, 5).ifPresent(rxd::rxd_5_actualDispenseUnit);
    getField(fields, 7).ifPresent(rxd::rxd_7_prescriptionNumber);
    return rxd.build();
  }
}
