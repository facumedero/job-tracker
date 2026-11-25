import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import ApplicationList from './ApplicationList.jsx'

const applications = [
  {
    id: 1,
    company: 'Acme',
    title: 'Java Developer',
    status: 'APPLIED',
    source: 'LinkedIn',
    url: null,
    appliedAt: '2026-11-10',
  },
  {
    id: 2,
    company: 'Globex',
    title: 'Full Stack Developer',
    status: 'TECHNICAL',
    source: null,
    url: 'https://example.com/job',
    appliedAt: '2026-11-05',
  },
]

describe('ApplicationList', () => {
  it('muestra un mensaje cuando no hay postulaciones', () => {
    render(<ApplicationList applications={[]} onStatusChange={vi.fn()} onDelete={vi.fn()} />)

    expect(screen.getByText(/todavía no hay postulaciones/i)).toBeInTheDocument()
  })

  it('lista las postulaciones con su empresa y puesto', () => {
    render(<ApplicationList applications={applications} onStatusChange={vi.fn()} onDelete={vi.fn()} />)

    expect(screen.getByText('Acme')).toBeInTheDocument()
    expect(screen.getByText('Full Stack Developer')).toBeInTheDocument()
  })

  it('avisa el cambio de estado con el id y el nuevo valor', () => {
    const onStatusChange = vi.fn()
    render(<ApplicationList applications={applications} onStatusChange={onStatusChange} onDelete={vi.fn()} />)

    fireEvent.change(screen.getByLabelText('Estado de Acme'), { target: { value: 'OFFER' } })

    expect(onStatusChange).toHaveBeenCalledWith(1, 'OFFER')
  })

  it('avisa el id de la postulación a eliminar', () => {
    const onDelete = vi.fn()
    render(<ApplicationList applications={applications} onStatusChange={vi.fn()} onDelete={onDelete} />)

    fireEvent.click(screen.getAllByRole('button', { name: 'Eliminar' })[1])

    expect(onDelete).toHaveBeenCalledWith(2)
  })
})
