package org.hl7.segment;


import static org.hl7.segment.component.IComponent.CARET;
import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

/**
 * PID segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/PID">Standard hl7 v2.5 spec - PID segment</a>
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class PID implements ISegment {
  @Builder.Default
  private final String pid_2_patientId = RandomStringUtils.randomNumeric(6);
  @Builder.Default
  private final String pid_3_patient_identifier_list = EMPTY;
  @Builder.Default
  private final String pid_5_1_patient_last_name = EMPTY;
  @Builder.Default
  private final String pid_5_2_patient_first_name = EMPTY;
  @Builder.Default
  private final String pid_7_patient_dob = EMPTY;
  @Builder.Default
  private final String pid_8_patient_gender = EMPTY;

  public static final String segmentId = "PID";

  @Override
  public String print() {
    return segmentId + "|1|" + pid_2_patientId + "|" + pid_3_patient_identifier_list + "||" + pid_5_1_patient_last_name + "^"
        + pid_5_2_patient_first_name + "^^^^||" + pid_7_patient_dob + "|" + pid_8_patient_gender
        + "|||||||||||||||||||||||||||||||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static PID fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    PIDBuilder pid = PID.builder();
    getField(fields, 2).ifPresent(pid::pid_2_patientId);
    getField(fields, 3).ifPresent(pid::pid_3_patient_identifier_list);
    getField(fields, 5).ifPresent(field -> {
      String[] subFields = field.split("\\".concat(CARET));
      pid.pid_5_1_patient_last_name(subFields[0])
          .pid_5_2_patient_first_name(subFields[1]);
    });
    getField(fields, 7).ifPresent(pid::pid_7_patient_dob);
    getField(fields, 8).ifPresent(pid::pid_8_patient_gender);
    return pid.build();
  }
}