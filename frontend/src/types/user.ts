export type UserInfo = {
  generation: number;
  isMajor: boolean[];
  gender: boolean[];
  campus: number;
  classes: number;
  floor: number;
  like: string;
  hate: string;
  mbti: string;
  worry: string;
};

export type UserMoreInfo = {
  mbti: string;
  likes: string;
  hate: string;
  worry: string;
};

export type UserSsafyInfo = {
  profileImage: string;
  classes: number;
  floor: number;
};
