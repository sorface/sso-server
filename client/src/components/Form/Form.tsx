import { DetailedHTMLProps, FormEvent, FunctionComponent, InputHTMLAttributes, ReactNode, useEffect, useRef, useState } from 'react';
import { Captions } from '../../constants';
import { FormFields } from './FormFields';
import { useCsrfApi } from '../../hooks/useGetCsrf';

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
    error: propsError,
    submitCaption,
    children,
}) => {
    const { loadCsrfConfig, csrfConfigState } = useCsrfApi();
    const { csrfConfig, process: { csrfConfigError } } = csrfConfigState;
    const [needRenderCsrf, setNeedRenderCsrf] = useState(false);
    const formRef = useRef<HTMLFormElement | null>(null);
    const error = propsError || csrfConfigError;

    if (needRenderCsrf && csrfConfig && formRef) {
        formRef.current?.submit();
    };

    useEffect(() => {
        if (!csrfConfig) {
            return;
        }
        setNeedRenderCsrf(true);
    }, [csrfConfig]);

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        loadCsrfConfig();
    };

    return (
        <form
            ref={formRef}
            action={htmlAction}
            method={htmlMethod}
            className={className}
            onSubmit={csrfConfig ? undefined : handleSubmit}
        >
            <FormFields fields={fields} fieldErrors={fieldErrors} />
            <input type='hidden' name={csrfConfig?.parameterName} value={csrfConfig?.token} />
            {submitCaption && <input type="submit" value={submitCaption} />}
            <div className='form-status'>
                {loading && <span>{Captions.Loading}...</span>}
                {error && <span className='form-field-error'>{Captions.Error}: {error}</span>}
            </div>
            {children && <div className='form-additional-content'>{children}</div>}
        </form>
    );
};
