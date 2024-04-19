import { ChangeEvent, FunctionComponent, useContext, useState } from 'react';
import defaultAvatarImage from './img/anonymus_avatar.png';
import { useLogout } from '../../hooks/useLogout';
import { Captions, IconNames } from "../../constants";
import { Icon } from '../../components/Icon/Icon';
import { AuthContext } from '../../context/AuthContext';

import './Account.css';

interface ProfileField {
    name: string;
    value: string | undefined;
    editable: boolean;
}

export const Account: FunctionComponent = () => {
    const account = useContext(AuthContext);
    const { logoutFetch } = useLogout();
    const [editedFieldName, setEditedFieldName] = useState('');
    const [editedFieldValue, setEditedFieldValue] = useState('');

    const handleLogout = () => {
        logoutFetch(undefined)
    }

    const handleEditField = (fieldName: string, initialValue: string) => {
        setEditedFieldName(fieldName);
        setEditedFieldValue(initialValue);
    };

    const handleEditedFieldChangeValue = (e: ChangeEvent<HTMLInputElement>) => {
        setEditedFieldValue(e.target.value);
    };

    const handleEditedFieldCancel = () => {
        setEditedFieldName('');
    };

    const handleEditedFieldSave = () => {
        console.log('field save', editedFieldName, editedFieldValue);
        handleEditedFieldCancel();
    };

    const fields: ProfileField[] = [
        {
            name: 'id',
            value: account?.id,
            editable: false,
        },
        {
            name: 'email',
            value: account?.email,
            editable: false,
        },
        {
            name: 'first name',
            value: account?.firstName,
            editable: true,
        },
        {
            name: 'last name',
            value: account?.lastName,
            editable: true,
        },
    ];

    return (
        <div className='account-page'>
            <div className="avatar">
                <img src={account?.avatar || defaultAvatarImage} alt="avatar" />
            </div>
            <table className="user-data-table">
                {fields.map(({ name, value, editable }) => (
                    <tr key={name}>
                        <td className="bold left">{name}</td>
                        {name !== editedFieldName ? (
                            <td className="right">
                                <div className="field-value">{value || Captions.Unknown}</div>
                                {editable && (
                                    <div className="field-action" onClick={() => handleEditField(name, value || '')}>
                                        <Icon name={IconNames.Create} />
                                    </div>
                                )}
                            </td>
                        ) : (
                            <td className="right">
                                <input className="field-value" type="text" value={editedFieldValue} onChange={handleEditedFieldChangeValue} />
                                <div className="field-action" onClick={handleEditedFieldSave}>
                                    <Icon name={IconNames.Checkmark} />
                                </div>
                                <div className="field-action" onClick={handleEditedFieldCancel}>
                                    <Icon name={IconNames.Close} />
                                </div>
                            </td>
                        )}
                    </tr>
                ))}
            </table>

            <button className="exit-button danger" onClick={handleLogout}>{Captions.Logout}</button>
        </div>
    );
};
