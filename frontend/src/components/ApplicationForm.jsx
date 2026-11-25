import { useState } from 'react'
import { STATUSES } from '../constants.js'

const emptyForm = {
  company: '',
  title: '',
  status: 'APPLIED',
  source: '',
  url: '',
  notes: '',
}

export default function ApplicationForm({ onSubmit }) {
  const [form, setForm] = useState(emptyForm)
  const [saving, setSaving] = useState(false)

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSaving(true)
    try {
      // Los campos opcionales vacíos se envían como null para no guardar strings vacíos.
      const payload = Object.fromEntries(
        Object.entries(form).map(([key, value]) => [key, value.trim() === '' ? null : value.trim()]),
      )
      await onSubmit(payload)
      setForm(emptyForm)
    } finally {
      setSaving(false)
    }
  }

  return (
    <form className="card form" onSubmit={handleSubmit}>
      <h2>Nueva postulación</h2>

      <label>
        Empresa *
        <input name="company" value={form.company} onChange={handleChange} required maxLength={120} />
      </label>

      <label>
        Puesto *
        <input name="title" value={form.title} onChange={handleChange} required maxLength={120} />
      </label>

      <label>
        Estado
        <select name="status" value={form.status} onChange={handleChange}>
          {STATUSES.map((status) => (
            <option key={status.value} value={status.value}>
              {status.label}
            </option>
          ))}
        </select>
      </label>

      <label>
        Origen
        <input name="source" value={form.source} onChange={handleChange} placeholder="LinkedIn, recruiter..." maxLength={60} />
      </label>

      <label>
        Link a la oferta
        <input name="url" type="url" value={form.url} onChange={handleChange} maxLength={500} />
      </label>

      <label>
        Notas
        <textarea name="notes" value={form.notes} onChange={handleChange} rows={3} maxLength={2000} />
      </label>

      <button type="submit" disabled={saving}>
        {saving ? 'Guardando...' : 'Agregar'}
      </button>
    </form>
  )
}
