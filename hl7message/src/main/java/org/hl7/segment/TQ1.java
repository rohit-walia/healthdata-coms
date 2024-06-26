package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.LinkedList;
import java.util.List;

/**
 * TQ1 segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/TQ1">Standard hl7 v2.5 spec - TQ1 segment</a>
 */
@Setter
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class TQ1 implements ISegment {
  public static final String segmentId = "TQ1";
  @NonNull
  private final String tq1_1_setId;
  @Builder.Default
  private String tq1_2_quantity = EMPTY;
  @Builder.Default
  private String tq1_3_repeatPattern = EMPTY;
  @Builder.Default
  private String tq1_4_explicit_time = EMPTY;
  @Builder.Default
  private String tq1_7_startDateTime = EMPTY;
  @Builder.Default
  private String tq1_8_endDateTime = EMPTY;
  @Builder.Default
  private String tq1_9_priority = EMPTY;
  @Builder.Default
  private String tq1_11_admin_instructions = EMPTY;
  @Builder.Default
  private String tq1_12_conjunction = EMPTY;

  /**
   * Converts String to object that implements ISegment.
   */
  public static TQ1 fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!fields[0].equals(segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    TQ1Builder tq1 = TQ1.builder();

    // required fields
    tq1.tq1_1_setId(getField(fields, 1).orElseThrow());

    // optional fields
    getField(fields, 2).ifPresent(tq1::tq1_2_quantity);
    getField(fields, 3).ifPresent(tq1::tq1_3_repeatPattern);
    getField(fields, 4).ifPresent(tq1::tq1_4_explicit_time);
    getField(fields, 7).ifPresent(tq1::tq1_7_startDateTime);
    getField(fields, 8).ifPresent(tq1::tq1_8_endDateTime);
    getField(fields, 9).ifPresent(tq1::tq1_9_priority);
    getField(fields, 11).ifPresent(tq1::tq1_11_admin_instructions);
    getField(fields, 12).ifPresent(tq1::tq1_12_conjunction);
    return tq1.build();
  }

  @Override
  public String print() {
    return segmentId + "|" + tq1_1_setId + "|" + tq1_2_quantity + "|" + tq1_3_repeatPattern + "|" + tq1_4_explicit_time
        + "|||" + tq1_7_startDateTime + "|" + tq1_8_endDateTime + "|" + tq1_9_priority + "||" + tq1_11_admin_instructions
        + "|" + tq1_12_conjunction + "||";
  }

  /**
   * Convert this object to mutable list of TQ1 objects. This is required as replacement for not using Singular
   * annotation in Hl7Message. Using Singular annotation implemented an unmodifiable list.
   *
   * @return List of TQ1 objects
   */
  public List<TQ1> toList() {
    List<TQ1> tq1List = new LinkedList<>();
    tq1List.add(this);
    return tq1List;
  }
}
