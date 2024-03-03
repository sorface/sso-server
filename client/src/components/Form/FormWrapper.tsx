import {FunctionComponent, ReactNode} from 'react';
import {PageLogo} from '../PageLogo/PageLogo';

export interface FormWrapperProps {
    children?: ReactNode;
}

export const FormWrapper: FunctionComponent<FormWrapperProps> = ({children}) => {
    return (
        <div className='form'>
            <div className='form-page-logo'>
                <PageLogo/>
            </div>
            <div className='form-content'>
                {children}
            </div>
        </div>
    );
};
