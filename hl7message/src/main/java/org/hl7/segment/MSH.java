package org.hl7.segment;

import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.RandomStringUtils;
import org.hl7.utils.Hl7DateUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MSG segment of an HL7 message.
 *
 * @see <a href="https://hl7-definition.caristix.com/v2/HL7v2.5.1/Segments/MSH">Standard hl7 v2.5 spec - MSH segment</a>
 */
@Setter
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class MSH implements ISegment {
  @NonNull
  private String msh_3_sendingSystem;
  @Builder.Default
  private String msh_4_sendingFacility = EMPTY;
  @Builder.Default
  private String msh_5_receivingSystem = EMPTY;
  @NonNull
  private String msh_6_receivingFacility;
  @Builder.Default
  private String msh_7_messageDatetime = Hl7DateUtils.formatToHl7(LocalDateTime.now());
  @NonNull
  private String msh_9_messageType;
  @Builder.Default
  private String msh_10_messageId = RandomStringUtils.randomNumeric(7);
  @Builder.Default
  private String msh_12_versionId = "2.5";

  public static final String segmentId = "MSH";

  @Override
  public String print() {
    return segmentId + "|^~\\&|" + msh_3_sendingSystem + "|" + msh_4_sendingFacility + "|"
        + msh_5_receivingSystem + "|" + msh_6_receivingFacility + "|" + msh_7_messageDatetime + "||" + msh_9_messageType
        + "|" + msh_10_messageId + "|P|" + msh_12_versionId + "||||||ASCII|||";
  }

  /**
   * Converts String to object that implements ISegment.
   */
  public static MSH fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    MSHBuilder msh = MSH.builder();
    getField(fields, 2).ifPresent(msh::msh_3_sendingSystem);
    getField(fields, 3).ifPresent(msh::msh_4_sendingFacility);
    getField(fields, 4).ifPresent(msh::msh_5_receivingSystem);
    getField(fields, 5).ifPresent(msh::msh_6_receivingFacility);
    getField(fields, 6).ifPresent(msh::msh_7_messageDatetime);
    getField(fields, 8).ifPresent(msh::msh_9_messageType);
    getField(fields, 9).ifPresent(msh::msh_10_messageId);
    getField(fields, 11).ifPresent(msh::msh_12_versionId);
    return msh.build();
  }
}
