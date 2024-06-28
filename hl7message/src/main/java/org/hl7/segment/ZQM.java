package org.hl7.segment;

import static org.hl7.segment.ISegment.PIPE;
import static org.hl7.utils.Hl7MsgUtils.getField;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

/**
 * ZQM is a custom segment for some specific interfaces.
 */
@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ZQM {
  public static final String segmentId = "ZQM";

  @Builder.Default
  private String zqm_3_barCode = "";
  @Builder.Default
  private String zqm_6_vitalList = "";
  @Builder.Default
  private String zqm_9_isSelfAdminOrSlidingScale = "";
  @Builder.Default
  private String zqm_10_brandNameEquivalent = "";

  /**
   * Converts String to object that implements ISegment.
   */
  public static ZQM fromString(String segment) {
    String[] fields = segment.split("\\".concat(PIPE), -1);

    if (!Objects.equals(fields[0], segmentId)) {
      throw new IllegalArgumentException("Invalid message segment [" + fields[0] + "]. Expected [" + segmentId + "]");
    }

    ZQMBuilder zqm = ZQM.builder();
    getField(fields, 3).ifPresent(zqm::zqm_3_barCode);
    getField(fields, 6).ifPresent(zqm::zqm_6_vitalList);
    getField(fields, 9).ifPresent(zqm::zqm_9_isSelfAdminOrSlidingScale);
    getField(fields, 10).ifPresent(zqm::zqm_10_brandNameEquivalent);
    return zqm.build();
  }

  public String print() {
    return segmentId + "|||" + zqm_3_barCode + "|||" + zqm_6_vitalList + "|||" + zqm_9_isSelfAdminOrSlidingScale
        + "|" + zqm_10_brandNameEquivalent + "||";
  }
}
