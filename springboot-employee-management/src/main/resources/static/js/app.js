const { createApp, ref, onMounted } = Vue;
const { ElMessage } = ElementPlus;

const app = createApp({
    setup() {
        const users = ref([]);
        const currentUser = ref(null);
        const dialogVisible = ref(false);
        const currentView = ref('home');
        const attendanceData = ref([]);
        const salaryData = ref([]);
        const roleData = ref([]);
        const permissionData = ref([]);

        // 获取员工列表
        const fetchUsers = async () => {
            try {
                const response = await axios.get('/employee/list');
                if (response.data.code === 1) {
                    users.value = response.data.data;
                } else {
                    ElMessage.error('获取员工列表失败');
                }
            } catch (error) {
                console.error('Error fetching users:', error);
                if (error.response && error.response.status === 401) {
                    window.location.reload();
                } else {
                    ElMessage.error('获取员工列表失败');
                }
            }
        };

        // 获取考勤数据
        const fetchAttendance = async () => {
            try {
                const response = await axios.get('/attendance/list');
                if (response.data.code === 1) {
                    attendanceData.value = response.data.data;
                } else {
                    ElMessage.error('获取考勤数据失败');
                }
            } catch (error) {
                console.error('Error fetching attendance:', error);
                if (error.response && error.response.status === 401) {
                    window.location.reload();
                } else {
                    ElMessage.error('获取考勤数据失败');
                }
            }
        };

        // 获取薪酬数据
        const fetchSalary = async () => {
            try {
                const response = await axios.get('/salary/list');
                if (response.data.code === 1) {
                    salaryData.value = response.data.data;
                } else {
                    ElMessage.error('获取薪酬数据失败');
                }
            } catch (error) {
                console.error('Error fetching salary:', error);
                if (error.response && error.response.status === 401) {
                    window.location.reload();
                } else {
                    ElMessage.error('获取薪酬数据失败');
                }
            }
        };

        // 获取系统数据
        const fetchSystemData = async () => {
            try {
                const [rolesResponse, permissionsResponse] = await Promise.all([
                    axios.get('/system/roles'),
                    axios.get('/system/permissions')
                ]);

                if (rolesResponse.data.code === 1) {
                    roleData.value = rolesResponse.data.data;
                }
                if (permissionsResponse.data.code === 1) {
                    permissionData.value = permissionsResponse.data.data;
                }
            } catch (error) {
                console.error('Error fetching system data:', error);
                if (error.response && error.response.status === 401) {
                    window.location.reload();
                } else {
                    ElMessage.error('获取系统数据失败');
                }
            }
        };

        // 查看用户详情
        const viewUser = async (id) => {
            try {
                const response = await axios.get(`/employee/view/${id}`);
                if (response.data.code === 1) {
                    currentUser.value = response.data.data;
                    dialogVisible.value = true;
                } else {
                    ElMessage.error('获取用户详情失败');
                }
            } catch (error) {
                console.error('Error viewing user:', error);
                if (error.response && error.response.status === 401) {
                    window.location.reload();
                } else {
                    ElMessage.error('获取用户详情失败');
                }
            }
        };

        // 退出登录
        const handleLogout = async () => {
            try {
                const response = await axios.get('/logout');
                window.location.reload();
            } catch (error) {
                console.error('Error logging out:', error);
                ElMessage.error('退出登录失败');
            }
        };

        // 处理导航选择
        const handleSelect = (key) => {
            currentView.value = key;
            switch (key) {
                case 'employee':
                    fetchUsers();
                    break;
                case 'attendance':
                    fetchAttendance();
                    break;
                case 'salary':
                    fetchSalary();
                    break;
                case 'system':
                    fetchSystemData();
                    break;
            }
        };

        // 页面加载时获取用户列表
        onMounted(() => {
            currentView.value = 'home';
        });

        return {
            users,
            currentUser,
            dialogVisible,
            currentView,
            attendanceData,
            salaryData,
            roleData,
            permissionData,
            viewUser,
            handleLogout,
            handleSelect
        };
    }
});

// 使用Element Plus
app.use(ElementPlus);

// 挂载应用
app.mount('#app'); 