package org.hl7.segment;

import static org.hl7.segment.datatype.IComponent.CARET;
import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

/**
 * PV1 segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/PV1">Standard hl7 v2.5 spec - PV1 segment</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class PV1 implements ISegment {
  @Builder.Default
  private String pv1_3_4_patientLocationRoom = EMPTY;
  @Builder.Default
  private String pv1_3_4_patientLocationBed = EMPTY;
  @Builder.Default
  private String pv1_3_4_patientLocationFacility = EMPTY;
  @Builder.Default
  private String pv1_44_admitDateTime = EMPTY;

  public static final String segmentId = "PV1";

  @Override
  public String print() {
    return segmentId + "|1|I|^" + pv1_3_4_patientLocationRoom + "^" + pv1_3_4_patientLocationBed + "^"
        + pv1_3_4_patientLocationFacility + "^^^^|||||||||||||||||||||||||||||||||||||||||" + pv1_44_admitDateTime
        + "||||||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static PV1 fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    PV1Builder pv1 = PV1.builder();
    getField(fields, 1).ifPresent(field -> {
      String[] subFields = field.split("\\".concat(CARET));
      pv1.pv1_3_4_patientLocationRoom(subFields[0])
          .pv1_3_4_patientLocationBed(subFields[1])
          .pv1_3_4_patientLocationFacility(subFields[2]);
    });
    getField(fields, 43).ifPresent(pv1::pv1_44_admitDateTime);
    return pv1.build();
  }
}
