export class AcademyProject {
    id: number;
    name: string;
    description: string;
    startDate: string;
    altStartDate: string;
    academy_Programs: AcademyProgram[];
}

export class AcademyProgram {
    id: number;
    name: string;
    noOfWeeks: number;
    fee: number;
    description: string;
}

export class RegistrationInfo {
    lastName!: string;
    firstName!: string;
    phoneNumber!: string;
    email!: string;
    gender!: string;
    programId!: number;
}
