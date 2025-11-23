package uz.ems.maydon24.models.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonSerialize
public class Empty implements Serializable {
}