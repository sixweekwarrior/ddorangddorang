import {StyleSheet, View} from 'react-native';
import MenuTop from '../../molecules/menuTop';
import GlobalStyles from '../../../styles/GlobalStyles';
import BtnBig from '../../atoms/btnBig';
import {useState} from 'react';
import InfoSelectInput from '../../atoms/infoSelectInput';
import { userApi } from '../../../apis';

export const ReviseSsafy = ({navigation}: {navigation: any}) => {
  const classList = Array.from({length: 20}, (_, index) =>
    (index + 1).toString(),
  );
  const floorList = Array.from({length: 20}, (_, index) =>
    (index + 1).toString(),
  );

  const [inputValues, setInputValues] = useState({
    classes: '',
    floor: '',
  });

  const onInputChange = (title: number, value: number) => {
    setInputValues(prevState => ({
      ...prevState,
      [title]: value,
    }));
  };

  const handleSubmit = async () => {
    try {
      const res = await userApi.putSsafyInfo({
        classes: inputValues.classes,
        floor: inputValues.floor,
      });
      console.log('ssafy_info_updated', res);

      navigation.navigate('Navbar');
    } catch (error) {
      console.error('ssafy_info_ERROR', error);
    }
  };

  return (
    <View style={styles.container}>
      <MenuTop
        menu="기본 정보 수정"
        text={`SSAFY 교육생으로서 \n나의 정보를 입력해주세요.`}
      />
      <View style={styles.innerContainer}>
        <View style={[styles.flexColumn, {height: '50%', rowGap: 15}]}>
          <InfoSelectInput
            title="반"
            placeholder="반을 선택하세요"
            data={classList}
            setValue={data => onInputChange('class', data)}
          />
          <InfoSelectInput
            title="층"
            placeholder="층을 선택하세요"
            data={floorList}
            setValue={data => onInputChange('floor', data)}
          />
        </View>
      </View>
      <View style={styles.btnContainer}>
        <BtnBig text="수정완료" onPress={handleSubmit} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  innerContainer: {
    borderWidth: 0.5,
    flex: 1,
    marginLeft: 24,
    marginRight: 24,
    marginTop: 20,
    borderStyle: 'solid',
    borderRadius: 20,
    borderColor: GlobalStyles.grey_3.color,
  },
  btnContainer: {
    flex: 1,
    bottom: '25%',
  },
  flexColumn: {
    flex: 1,
    justifyContent: 'center',
    alignSelf: 'center',
    flexDirection: 'column',
    flexWrap: 'wrap',
  },
});

export default ReviseSsafy;
