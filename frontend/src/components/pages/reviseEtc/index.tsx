import {StyleSheet, View} from 'react-native';
import MenuTop from '../../molecules/menuTop';
import GlobalStyles from '../../../styles/GlobalStyles';
import BtnBig from '../../atoms/btnBig';
import InputInfoMolecules from '../../molecules/inputInfoMolecules';
import {useState} from 'react';

export const ReviseEtc = ({navigation}: {navigation: any}) => {
  const [inputValues, setInputValues] = useState({
    MBTI: '',
    currentConcern: '',
    likes: '',
    dislikes: '',
  });

  const handleInputChange = (title: string, value: string) => {
    setInputValues(prevState => ({
      ...prevState,
      [title]: value,
    }));
  };

  const handleSubmit = () => {
    console.log(inputValues);
    navigation.navigate('NavBar');
  };

  return (
    <View style={styles.container}>
      <MenuTop
        menu="기본 정보 수정"
        text={`SSAFY 교육생으로서 \n나의 정보를 입력해주세요.`}
      />
      <View style={styles.innerContainer}>
        <View style={[styles.flexColumn, {height: '50%', rowGap: 15}]}>
          <InputInfoMolecules
            title="MBTI"
            placeholder="MBTI를 입력해주세요"
            type="text"
            onInputChange={(text: string) => handleInputChange('MBTI', text)}
          />
          <InputInfoMolecules
            title="요즘 고민"
            placeholder="고민을 입력해주세요"
            type="text"
            onInputChange={(text: string) =>
              handleInputChange('currentConcern', text)
            }
          />
          <InputInfoMolecules
            title="좋아하는 것"
            placeholder="좋아하는 것을 입력해주세요"
            type="text"
            onInputChange={(text: string) => handleInputChange('likes', text)}
          />
          <InputInfoMolecules
            title="싫어하는 것"
            placeholder="싫어하는 것을 입력해주세요"
            type="text"
            onInputChange={(text: string) =>
              handleInputChange('dislikes', text)
            }
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

export default ReviseEtc;