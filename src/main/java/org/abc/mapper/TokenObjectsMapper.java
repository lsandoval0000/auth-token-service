package org.abc.mapper;

import org.abc.dto.TokenResponse;
import org.abc.entity.GeneratedTokenHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "cdi")
public interface TokenObjectsMapper {
    TokenResponse entityToResponse(GeneratedTokenHistory entity);
}
