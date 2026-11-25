import { useCallback, useEffect, useState } from 'react'
import ApplicationForm from './components/ApplicationForm.jsx'
import ApplicationList from './components/ApplicationList.jsx'
import { STATUSES } from './constants.js'
import {
  createApplication,
  deleteApplication,
  listApplications,
  updateStatus,
} from './api.js'

export default function App() {
  const [applications, setApplications] = useState([])
  const [filter, setFilter] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = useCallback(async () => {
    setLoading(true)
    try {
      setApplications(await listApplications(filter))
      setError(null)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [filter])

  useEffect(() => {
    load()
  }, [load])

  // Envuelve cada acción: si falla muestra el error, si sale bien recarga el listado.
  const run = async (action) => {
    try {
      await action()
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <main className="container">
      <header>
        <h1>Job Tracker</h1>
        <p className="muted">Seguimiento de postulaciones laborales</p>
      </header>

      {error && (
        <p role="alert" className="error">
          {error}
        </p>
      )}

      <div className="layout">
        <ApplicationForm onSubmit={(data) => run(() => createApplication(data))} />

        <section className="card">
          <div className="toolbar">
            <h2>Postulaciones</h2>
            <select
              aria-label="Filtrar por estado"
              value={filter}
              onChange={(event) => setFilter(event.target.value)}
            >
              <option value="">Todas</option>
              {STATUSES.map((status) => (
                <option key={status.value} value={status.value}>
                  {status.label}
                </option>
              ))}
            </select>
          </div>

          {loading ? (
            <p className="muted">Cargando...</p>
          ) : (
            <ApplicationList
              applications={applications}
              onStatusChange={(id, status) => run(() => updateStatus(id, status))}
              onDelete={(id) => run(() => deleteApplication(id))}
            />
          )}
        </section>
      </div>
    </main>
  )
}
