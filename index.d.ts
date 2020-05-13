export function supportNFC(): { supported: boolean; enabled: boolean };

export function listenNFCStatus(): (
  callback: (enabled: boolean) => void
) => void;

export function setCardContent(value: string): void;

export function setAidFilter(value: string): void;
