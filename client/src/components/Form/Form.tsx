import { DetailedHTMLProps, FunctionComponent, InputHTMLAttributes, ReactNode } from 'react';
import { Captions } from '../../constants';
import { FormFields } from './FormFields';

export type FieldErrors = Record<string, string>;

export interface Field extends DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> {
    name: string;
}

export interface FormProps {
    fields: Field[];
    fieldErrors: FieldErrors;
    htmlAction: string;
    htmlMethod: string;
    className?: string;
    loading?: boolean;
    error?: string | null;
    submitCaption?: string;
    children?: ReactNode;
}

export const Form: FunctionComponent<FormProps> = ({
    fields,
    fieldErrors,
    htmlAction,
    htmlMethod,
    className,
    loading,
    error,
    submitCaption,
    children,
}) => {
    return (
        <form action={htmlAction} method={htmlMethod} className={className}>
            <FormFields fields={fields} fieldErrors={fieldErrors} />
            {submitCaption && <input type="submit" value={submitCaption} />}
            <div className='form-status'>
                {loading && <span>{Captions.Loading}...</span>}
                {error && <span className='form-field-error'>{Captions.Error}: {error}</span>}
            </div>
            {children && <div className='form-additional-content'>{children}</div>}
        </form>
    );
};
