import { FunctionComponent, DetailedHTMLProps, InputHTMLAttributes, FormEvent, ReactNode } from 'react';
import { Captions } from '../../constants';

import './Form.css';

export interface Field extends DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> {
  name: string;
  error?: string | null;
}

interface FormProps {
  fields: Field[];
  loading: boolean;
  error?: string | null;
  submitCaption: string;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  children?: ReactNode;
}

export const Form: FunctionComponent<FormProps> = ({
  fields,
  loading,
  error,
  submitCaption,
  onSubmit,
  children,
}) => {
  return (
    <form className='form' onSubmit={onSubmit}>
      <div className='form-content'>
        {fields.map(field => (
          <div key={field.name} className='form-field-wrapper'>
            <label htmlFor={field.name}>{field.placeholder}:</label>
            <input id={field.name} {...field} />
            {!!field.error && (
              <div className='form-field-error'>{field.error}</div>
            )}
          </div>
        ))}
        <input type="submit" value={submitCaption} />
        <div className='form-status'>
          {loading && <span>{Captions.Loading}...</span>}
          {error && <span className='form-field-error'>{Captions.Error}: {error}</span>}
        </div>
        {children && <div className='form-additional-content'>{children}</div>}
      </div>
    </form>
  );
};
