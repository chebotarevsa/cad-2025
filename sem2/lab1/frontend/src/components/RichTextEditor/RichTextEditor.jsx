import { useMemo } from 'react'
import ReactQuill from 'react-quill-new'
import 'react-quill-new/dist/quill.snow.css'
import './RichTextEditor.css'

const MODULES = {
  toolbar: [
    ['bold', 'italic', 'underline', 'strike'],
    [{ list: 'ordered' }, { list: 'bullet' }],
    ['link'],
    ['clean'],
  ],
}
const FORMATS = ['bold', 'italic', 'underline', 'strike', 'list', 'link']

export function richEmpty(html) {
  if (!html) return true
  return html.replace(/<[^>]*>/g, '').replace(/\s|&nbsp;/g, '') === ''
}

function looksLikeHtml(s) {
  return /<[a-z][\s\S]*>/i.test(s || '')
}

function escapeHtml(s) {
  return (s || '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

export function prepareRichHtml(value) {
  if (!value) return ''
  if (!looksLikeHtml(value)) {
    return normalizeTextFragment(escapeHtml(value)).replace(/\n/g, '<br>')
  }
  const html = value.replace(/<a\s/gi, '<a target="_blank" rel="noopener noreferrer" ')



  return html
    .split(/(<[^>]+>)/g)
    .map((part) => part.startsWith('<') ? part : normalizeTextFragment(part))
    .join('')
}

function normalizeTextFragment(text) {
  return protectCompoundHyphens(
    text.replace(/(?:&nbsp;|&#160;|&#x0*a0;|\u00a0)/gi, ' '),
  )
}

function protectCompoundHyphens(text) {
  return text.replace(/([\p{L}\p{N}])-(?=[\p{L}\p{N}])/gu, '$1‑')
}

export default function RichTextEditor({ value, onChange, placeholder }) {
  const modules = useMemo(() => MODULES, [])
  return (
    <div className="rte">
      <ReactQuill
        theme="snow"
        value={value || ''}
        onChange={onChange}
        modules={modules}
        formats={FORMATS}
        placeholder={placeholder}
      />
    </div>
  )
}

export function RichText({ value, className = '' }) {
  return (
    <div
      className={'rte-content ' + className}
      dangerouslySetInnerHTML={{ __html: prepareRichHtml(value) }}
    />
  )
}
