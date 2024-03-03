import {FunctionComponent} from 'react';
import {FormProps} from './Form';

type FormFieldsProps = Pick<FormProps, 'fields'>;

export const FormFields: FunctionComponent<FormFieldsProps> = ({
                                                                   fields,
                                                               }) => {
    return (
        <>
            {fields.map(field => (
                <div key={field.name} className='form-field-wrapper'>
                    <label htmlFor={field.name}>{field.placeholder}:</label>
                    <input id={field.name} {...field} />
                    {!!field.error && (
                        <div className='form-field-error'>{field.error}</div>
                    )}
                </div>
            ))}
        </>
    );
};
