import {Pressable, StyleSheet, Text} from 'react-native';
import GlobalStyles from '../../../styles/GlobalStyles';

type MissionTabProps = {
  day: string;
  content: string;
  done: boolean;
};
const MissionTab = ({day, content, done}: MissionTabProps) => {
  return (
    <Pressable style={style.bottomTop}>
      <Text style={style.titleFont}>{day}</Text>
      <Text style={style.miniFont}>{content}</Text>
      <Pressable style={style.circle}>
        <Text style={style.midBoldFont}>{done ? 'CLEAR' : 'FAIL'}</Text>
      </Pressable>
    </Pressable>
  );
};

const style = StyleSheet.create({
  bottomTop: {
    marginTop: 24,
    marginLeft: 24,
    borderWidth: 0.5,
    borderStyle: 'solid',
    borderRadius: 20,
    borderColor: GlobalStyles.grey_3.color,
  },
  circle: {
    backgroundColor: GlobalStyles.orange.color,
    width: 60,
    height: 60,
    borderRadius: 100,
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
    marginHorizontal: 15,
    marginBottom: 10,
  },
  midBoldFont: {
    fontFamily: 'NotoSansKR-Bold',
    color: GlobalStyles.white_2.color,
    verticalAlign: 'middle',
    fontSize: 14,
  },
  miniFont: {
    fontFamily: GlobalStyles.home_title.fontFamily,
    color: GlobalStyles.grey_3.color,
    fontSize: 10,
    marginLeft: 15,
    marginTop: -15,
  },
  titleFont: {
    fontFamily: GlobalStyles.home_title.fontFamily,
    fontSize: GlobalStyles.home_title.fontSize,
    letterSpacing: -1,
    color: GlobalStyles.grey_2.color,
    marginLeft: 15,
  },
});

export default MissionTab;