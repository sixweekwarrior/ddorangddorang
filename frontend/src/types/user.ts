export type UserInfo = {
  id: number;
  name: string;
  generation: number;
  isMajor: boolean;
  gender: boolean;
  campus: number;
  classes: number;
  floor: number;
  profileImage: string;
  likes: string;
  hate: string;
  mbti: string;
  worry: string;
  roomId: number;
  status: number;
  mood: string;
  color: string;
  profile: string;
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

export type UserProfile = {
  userId: number;
  name: string;
  profileImage: string;
  isMajor: boolean;
  classes: number;
};

export type UserDailyInfo = {
  mood: string;
  color: string;
};

export type HomeInfo = {
  color: string;
  mood: string;
  dday: number;
  isMissionDone: boolean;
  missionTitle: string;
  missionId: number;
  missionPerformId: number;
  dayCount: number;
};

export type OpinionInfo = {
  content: string;
};

export type ManitiInfo = {
  classes: number;
  isMajor: boolean;
  name: string;
  generation: number;
};

export type GuessInfo = {
  me: UserProfile;
  guessUser: UserProfile;
  manito: UserProfile;
};
