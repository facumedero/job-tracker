const BASE = '/api/applications'

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  if (!response.ok) {
    let message = `Error ${response.status}`
    try {
      // El backend responde errores con formato ProblemDetail (campo "detail").
      const problem = await response.json()
      message = problem.detail || message
    } catch {
      // La respuesta no tenía cuerpo JSON; se queda el mensaje genérico.
    }
    throw new Error(message)
  }

  return response.status === 204 ? null : response.json()
}

export const listApplications = (status) =>
  request(status ? `${BASE}?status=${encodeURIComponent(status)}` : BASE)

export const createApplication = (data) =>
  request(BASE, { method: 'POST', body: JSON.stringify(data) })

export const updateStatus = (id, status) =>
  request(`${BASE}/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) })

export const deleteApplication = (id) => request(`${BASE}/${id}`, { method: 'DELETE' })
