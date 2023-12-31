import {
  View,
  StyleSheet,
  Image,
  Text,
  TouchableOpacity,
  Pressable,
} from 'react-native';
import {UserProfile} from '../../../types/user';
import GlobalStyles, {height, width} from '../../../styles/GlobalStyles';
import {useEffect, useState} from 'react';
import Modal from 'react-native-modal';
import BtnSm from '../btnSm';
import closeImg from '../../../assets/closeImg.png';
import {guessApi} from '../../../apis';

type ProfileProps = {
  selectedList: number[];
  setSelectedList: (value: number[]) => void;
  isAllChecked: boolean;
  toggle?: boolean;
};

type Profile = ProfileProps & UserProfile & any;

export const Profile = ({
  name,
  userId,
  classes,
  profileImage,
  isMajor,
  selectedList,
  setSelectedList,
  isAllChecked,
  toggle,
  navigation,
}: Profile): JSX.Element => {
  const [isSelected, setIsSelected] = useState<boolean>(
    selectedList.includes(userId),
  );
  const [isModalVisible, setModalVisible] = useState(false);
  const toggleModal = () => {
    setModalVisible(!isModalVisible);
  };

  const handleProfilePress = () => {
    if (toggle) {
      toggleProfile();
    } else {
      defaultProfileToggle();
    }
  };

  const toggleProfile = () => {
    setSelectedList([userId]);
    toggleModal();
  };

  const defaultProfileToggle = () => {
    if (isSelected) {
      // 이미 선택된 경우, 선택 해제
      setSelectedList(selectedList.filter((data: number) => data !== userId));
    } else {
      // 선택되지 않은 경우, 다른 선택 해제 후 현재 선택
      setSelectedList([userId, ...selectedList]);
    }
    setIsSelected(!isSelected);
  };

  useEffect(() => {
    setIsSelected(isAllChecked);
  }, [isAllChecked]);

  const changeManito = () => {
    try {
      const manitoId = selectedList[0];
      guessApi.postGuess(manitoId).then(data =>
        navigation.navigate('MatchStatus', {
          showNotice: true,
          profileImage: data.profileImage,
          manitoName: data.name,
        }),
      );
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <View style={styles.profileContainer}>
      <TouchableOpacity onPress={handleProfilePress}>
        <Image
          source={{uri: profileImage}}
          style={[styles.profilepic, isSelected && styles.selectedProfile]}
        />
      </TouchableOpacity>
      <Text style={styles.profilename}>{name}</Text>
      <Text style={styles.profiledetail}>
        {isMajor ? '전공' : '비전공'} | {classes}반
      </Text>
      <Modal
        isVisible={isModalVisible}
        onBackdropPress={() => setModalVisible(false)}>
        <View style={{flex: 1, justifyContent: 'center'}}>
          <View style={styles.modalContainer}>
            <Pressable style={styles.close} onPress={toggleModal}>
              <Image source={closeImg} style={styles.closeImg} />
            </Pressable>
            <View style={styles.noticeContainer}>
              <View style={styles.row}>
                <Text style={styles.noticeText}>예상 마니또를 </Text>
                <Text style={styles.nameText}>{name}</Text>
                <Text style={styles.noticeText}>님으로</Text>
              </View>
              <Text style={{...styles.noticeText, alignSelf: 'center'}}>
                설정하시겠어요?
              </Text>
              <View style={styles.btnContainer}>
                <BtnSm text="네!" onPress={changeManito} />
                <BtnSm text="아니요!" onPress={toggleModal} isDark={true} />
              </View>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
};

const styles = StyleSheet.create({
  profileContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    marginHorizontal: 7,
  },
  modalContainer: {
    backgroundColor: GlobalStyles.white_2.color,
    borderRadius: 30,
    width: '90%',
    height: 230,
    alignSelf: 'center',
    verticalAlign: 'middle',
  },
  row: {
    flexDirection: 'row',
    alignSelf: 'center',
  },
  noticeContainer: {
    height: '100%',
    justifyContent: 'center',
  },
  btnContainer: {
    marginTop: 20,
    flexDirection: 'row',
    columnGap: width * 10,
    alignSelf: 'center',
  },
  profilepic: {
    width: 60,
    height: 60,
    borderRadius: 100,
    borderWidth: 0.5,
    borderColor: GlobalStyles.grey_4.color,
  },
  profilename: {
    fontFamily: GlobalStyles.section_title.fontFamily,
    fontSize: height * 12,
    color: GlobalStyles.grey_2.color,
    marginTop: -8,
  },
  profiledetail: {
    fontFamily: GlobalStyles.sub_title.fontFamily,
    fontSize: height * 10,
    color: GlobalStyles.grey_2.color,
    marginTop: -20,
  },

  selectedProfile: {
    borderColor: GlobalStyles.orange.color,
    borderWidth: 3,
  },
  close: {
    right: height * 20,
    position: 'absolute',
  },
  closeImg: {
    width: 15,
    objectFit: 'scale-down',
  },
  noticeText: {
    fontFamily: GlobalStyles.content.fontFamily,
    fontSize: height * 15,
    color: GlobalStyles.black.color,
    lineHeight: height * 25,
  },
  nameText: {
    fontFamily: GlobalStyles.section_title.fontFamily,
    fontSize: height * 18,
    color: GlobalStyles.green.color,
    lineHeight: height * 25,
  },
});

export default Profile;
