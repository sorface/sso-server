import {DetailedHTMLProps, FormEvent, FunctionComponent, InputHTMLAttributes, ReactNode} from 'react';
import {Captions} from '../../constants';
import {FormWrapper} from './FormWrapper';
import {FormFields} from './FormFields';

import './Form.css';

export interface Field extends DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> {
    name: string;
    error?: string | null;
}

export interface FormProps {
    fields: Field[];
    loading: boolean;
    error?: string | null;
    submitCaption?: string;
    onSubmit?: (event: FormEvent<HTMLFormElement>) => void;
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
        <FormWrapper>
            <form onSubmit={onSubmit}>
                <FormFields fields={fields}/>
                {submitCaption && <input type="submit" value={submitCaption}/>}
                <div className='form-status'>
                    {loading && <span>{Captions.Loading}...</span>}
                    {error && <span className='form-field-error'>{Captions.Error}: {error}</span>}
                </div>
                {children && <div className='form-additional-content'>{children}</div>}
            </form>
        </FormWrapper>
    );
};
