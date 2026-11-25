export const STATUSES = [
  { value: 'APPLIED', label: 'Postulado' },
  { value: 'SCREENING', label: 'Screening' },
  { value: 'TECHNICAL', label: 'Entrevista técnica' },
  { value: 'OFFER', label: 'Oferta' },
  { value: 'REJECTED', label: 'Rechazado' },
]

export const statusLabel = (value) =>
  STATUSES.find((status) => status.value === value)?.label ?? value
