import {StyleSheet, Pressable, Text, Image} from 'react-native';
import GlobalStyles, {height, width} from '../../../styles/GlobalStyles';
import ArrowRight from '../../../assets/whiteArrowRightImg.png';

type BtnMidProps = {
  onPress: () => void;
  text: string;
};

export const BtnMid = ({onPress, text}: BtnMidProps) => {
  return (
    <Pressable onPress={onPress} style={styles.btnContainer}>
      <Text style={styles.btnText}>{text}</Text>
      <Image source={ArrowRight} style={styles.arrow} />
    </Pressable>
  );
};
const styles = StyleSheet.create({
  btnContainer: {
    width: width * 236,
    height: height * 40,
    backgroundColor: GlobalStyles.blue.color,
    borderRadius: 20,
    marginTop: 30,
    flexDirection: 'row',
    alignSelf: 'center',
    paddingLeft: '7%',
    paddingRight: '4%',
    justifyContent: 'space-between',
  },
  btnText: {
    fontFamily: GlobalStyles.btn.fontFamily,
    fontSize: 14,
    color: GlobalStyles.white_2.color,
    textAlign: 'left',
    verticalAlign: 'middle',
  },
  arrow: {
    width: 15,
    height: 13,
    objectFit: 'scale-down',
    marginTop: 13,
  },
});

export default BtnMid;