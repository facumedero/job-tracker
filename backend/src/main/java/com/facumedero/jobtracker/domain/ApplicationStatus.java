package com.facumedero.jobtracker.domain;

/**
 * Etapas por las que pasa una postulación.
 * Se persiste como texto (EnumType.STRING) para que reordenar o agregar valores no corrompa los datos.
 */
public enum ApplicationStatus {
    APPLIED,
    SCREENING,
    TECHNICAL,
    OFFER,
    REJECTED
}
