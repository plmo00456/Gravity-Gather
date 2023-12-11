
// 이메일 주소를 앞뒤 2글자를 제외하고 '*'로 표시
// plmo00456@gmail.com -> pl*****56@gmail.com
export function maskEmailFn(email) {
    const [localPart, domain] = email.split('@');

    if (localPart.length > 4) {
        const firstTwo = localPart.slice(0, 2);
        const lastTwo = localPart.slice(-2);
        const stars = '*'.repeat(localPart.length - 4);
        return `${firstTwo}${stars}${lastTwo}@${domain}`;
    } else {
        return email;
    }
}