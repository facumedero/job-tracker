import { STATUSES } from '../constants.js'

export default function ApplicationList({ applications, onStatusChange, onDelete }) {
  if (applications.length === 0) {
    return <p className="empty">Todavía no hay postulaciones para mostrar.</p>
  }

  return (
    <table className="table">
      <thead>
        <tr>
          <th>Empresa</th>
          <th>Puesto</th>
          <th>Estado</th>
          <th>Fecha</th>
          <th aria-label="Acciones" />
        </tr>
      </thead>
      <tbody>
        {applications.map((application) => (
          <tr key={application.id}>
            <td>
              {application.url ? (
                <a href={application.url} target="_blank" rel="noreferrer">
                  {application.company}
                </a>
              ) : (
                application.company
              )}
              {application.source && <small className="muted"> · {application.source}</small>}
            </td>
            <td>{application.title}</td>
            <td>
              <select
                aria-label={`Estado de ${application.company}`}
                value={application.status}
                onChange={(event) => onStatusChange(application.id, event.target.value)}
              >
                {STATUSES.map((status) => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </select>
            </td>
            <td>{application.appliedAt}</td>
            <td>
              <button type="button" className="danger" onClick={() => onDelete(application.id)}>
                Eliminar
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
