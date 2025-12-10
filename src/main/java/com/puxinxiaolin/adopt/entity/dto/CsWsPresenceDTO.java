package com.puxinxiaolin.adopt.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsWsPresenceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Boolean online;
}
