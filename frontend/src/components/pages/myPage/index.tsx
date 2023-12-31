import {View, StyleSheet, ScrollView} from 'react-native';
import MenuTop from '../../molecules/menuTop';
import GlobalStyles from '../../../styles/GlobalStyles';
import InfoBox from '../../organisms/infoBox';
import {useSetRecoilState} from 'recoil';
import {useEffect} from 'react';
import {userApi} from '../../../apis';
import user from '../../../modules/user';

export const MyPage = ({navigation}: {navigation: any}): JSX.Element => {
  const setUserInfo = useSetRecoilState(user.UserInfoState);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await userApi.getUser();
        await setUserInfo(data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <ScrollView style={styles.container}>
      <MenuTop
        menu="마이페이지"
        text={'오늘의 정보를 설정하고 \n추가정보를 입력해보세요!'}
      />
      <View style={styles.topContainer}>
        <InfoBox navigation={navigation} destination="InfoToday" />
      </View>
      <View style={styles.midContainer}>
        <InfoBox navigation={navigation} destination="InfoSsafy" />
      </View>
      <View style={styles.bottomContainer}>
        <InfoBox navigation={navigation} destination="InfoEtc" />
      </View>
      <View style={styles.emptyContainer}>
        <InfoBox navigation={navigation} destination="SendOpinion" />
      </View>
      <View style={styles.emptyContainer}>
        <InfoBox navigation={navigation} destination="Logout" />
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: GlobalStyles.white_2.color,
  },
  topContainer: {
    flex: 0.5,
    marginVertical: 24,
  },
  midContainer: {
    flex: 0.5,
  },
  bottomContainer: {
    flex: 0.7,
    marginVertical: 24,
  },
  emptyContainer: {
    flex: 0.2,
    marginBottom: 24,
  },
});

export default MyPage;
