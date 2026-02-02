package br.com.relojoaria.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserInfoDto {
    private Long id;
    private String name;
    private Set<String> roles;
}
