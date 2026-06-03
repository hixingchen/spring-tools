<template>
  <div class="linux-commands">
    <h2>Linux 常见命令</h2>
    <el-input
      v-model="search"
      placeholder="搜索命令名称、说明或示例..."
      clearable
      prefix-icon="Search"
      size="large"
      class="search-input"
    />
    <el-collapse v-model="activeNames">
      <el-collapse-item
        v-for="category in filteredCategories"
        :key="category.name"
        :title="`${category.name}（${category.commands.length}）`"
        :name="category.name"
      >
        <el-table :data="category.commands" stripe border style="width: 100%">
          <el-table-column prop="name" label="命令" width="120" />
          <el-table-column prop="syntax" label="语法" min-width="200" />
          <el-table-column prop="desc" label="说明" min-width="200" />
          <el-table-column prop="example" label="常用示例" min-width="200" />
        </el-table>
      </el-collapse-item>
    </el-collapse>
    <el-empty v-if="filteredCategories.length === 0" description="没有匹配的命令" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const search = ref('')
const activeNames = ref([])

const categories = [
  {
    name: '文件操作',
    commands: [
      { name: 'ls', syntax: 'ls [选项] [文件]', desc: '列出目录内容', example: 'ls -la' },
      { name: 'cp', syntax: 'cp [选项] 源文件 目标', desc: '复制文件或目录', example: 'cp -r dir1 dir2' },
      { name: 'mv', syntax: 'mv [选项] 源文件 目标', desc: '移动或重命名文件', example: 'mv old.txt new.txt' },
      { name: 'rm', syntax: 'rm [选项] 文件', desc: '删除文件或目录', example: 'rm -rf dir' },
      { name: 'touch', syntax: 'touch [选项] 文件', desc: '创建空文件或更新时间戳', example: 'touch newfile.txt' },
      { name: 'ln', syntax: 'ln [选项] 源文件 目标', desc: '创建链接', example: 'ln -s /path/file link' },
    ]
  },
  {
    name: '目录操作',
    commands: [
      { name: 'cd', syntax: 'cd [目录]', desc: '切换工作目录', example: 'cd /home/user' },
      { name: 'pwd', syntax: 'pwd', desc: '显示当前工作目录', example: 'pwd' },
      { name: 'mkdir', syntax: 'mkdir [选项] 目录', desc: '创建目录', example: 'mkdir -p a/b/c' },
      { name: 'rmdir', syntax: 'rmdir [选项] 目录', desc: '删除空目录', example: 'rmdir empty_dir' },
      { name: 'tree', syntax: 'tree [选项] [目录]', desc: '以树状图列出目录结构', example: 'tree -L 2' },
    ]
  },
  {
    name: '查看文件内容',
    commands: [
      { name: 'cat', syntax: 'cat [选项] 文件', desc: '显示文件内容', example: 'cat file.txt' },
      { name: 'head', syntax: 'head [选项] 文件', desc: '显示文件开头内容', example: 'head -n 20 file.txt' },
      { name: 'tail', syntax: 'tail [选项] 文件', desc: '显示文件末尾内容', example: 'tail -f /var/log/syslog' },
      { name: 'less', syntax: 'less [选项] 文件', desc: '分页查看文件内容', example: 'less largefile.log' },
      { name: 'grep', syntax: 'grep [选项] 模式 文件', desc: '搜索文件内容', example: 'grep -rn "keyword" /path' },
      { name: 'wc', syntax: 'wc [选项] 文件', desc: '统计行数、字数、字节数', example: 'wc -l file.txt' },
    ]
  },
  {
    name: '权限管理',
    commands: [
      { name: 'chmod', syntax: 'chmod [权限] 文件', desc: '修改文件权限', example: 'chmod 755 script.sh' },
      { name: 'chown', syntax: 'chown [用户:组] 文件', desc: '修改文件所有者', example: 'chown user:group file' },
      { name: 'chgrp', syntax: 'chgrp [组] 文件', desc: '修改文件所属组', example: 'chgrp developers project' },
      { name: 'umask', syntax: 'umask [权限掩码]', desc: '设置默认文件权限掩码', example: 'umask 022' },
    ]
  },
  {
    name: '压缩解压',
    commands: [
      { name: 'tar', syntax: 'tar [选项] 文件', desc: '打包和解包文件', example: 'tar -czvf archive.tar.gz dir/' },
      { name: 'gzip', syntax: 'gzip [选项] 文件', desc: '压缩文件（.gz）', example: 'gzip file.txt' },
      { name: 'gunzip', syntax: 'gunzip [选项] 文件', desc: '解压 .gz 文件', example: 'gunzip file.txt.gz' },
      { name: 'zip', syntax: 'zip [选项] 压缩包 文件', desc: '创建 .zip 压缩包', example: 'zip -r archive.zip dir/' },
      { name: 'unzip', syntax: 'unzip [选项] 文件', desc: '解压 .zip 文件', example: 'unzip archive.zip -d output/' },
    ]
  },
  {
    name: '网络命令',
    commands: [
      { name: 'ping', syntax: 'ping [选项] 主机', desc: '测试网络连通性', example: 'ping -c 4 google.com' },
      { name: 'curl', syntax: 'curl [选项] URL', desc: '发送 HTTP 请求', example: 'curl -X POST -d "a=1" url' },
      { name: 'wget', syntax: 'wget [选项] URL', desc: '下载文件', example: 'wget https://example.com/file' },
      { name: 'netstat', syntax: 'netstat [选项]', desc: '显示网络状态', example: 'netstat -tuln' },
      { name: 'ssh', syntax: 'ssh [选项] 用户@主机', desc: '远程登录', example: 'ssh user@192.168.1.100' },
      { name: 'scp', syntax: 'scp [选项] 源 目标', desc: '远程复制文件', example: 'scp file.txt user@host:/path' },
    ]
  },
  {
    name: '系统信息',
    commands: [
      { name: 'uname', syntax: 'uname [选项]', desc: '显示系统信息', example: 'uname -a' },
      { name: 'top', syntax: 'top', desc: '实时显示系统资源使用', example: 'top' },
      { name: 'df', syntax: 'df [选项]', desc: '显示磁盘空间使用情况', example: 'df -h' },
      { name: 'du', syntax: 'du [选项] [目录]', desc: '显示目录或文件大小', example: 'du -sh /var/log' },
      { name: 'free', syntax: 'free [选项]', desc: '显示内存使用情况', example: 'free -h' },
      { name: 'uptime', syntax: 'uptime', desc: '显示系统运行时间', example: 'uptime' },
    ]
  },
  {
    name: '进程管理',
    commands: [
      { name: 'ps', syntax: 'ps [选项]', desc: '查看进程状态', example: 'ps aux | grep nginx' },
      { name: 'kill', syntax: 'kill [信号] PID', desc: '终止进程', example: 'kill -9 1234' },
      { name: 'pkill', syntax: 'pkill [选项] 进程名', desc: '按名称终止进程', example: 'pkill -f python' },
      { name: 'nohup', syntax: 'nohup 命令 &', desc: '后台运行进程（不受终端关闭影响）', example: 'nohup ./server.sh &' },
      { name: 'systemctl', syntax: 'systemctl [命令] 服务', desc: '管理系统服务', example: 'systemctl restart nginx' },
      { name: 'journalctl', syntax: 'journalctl [选项]', desc: '查看系统日志', example: 'journalctl -u nginx -f' },
    ]
  },
  {
    name: '磁盘管理',
    commands: [
      { name: 'fdisk', syntax: 'fdisk [选项] 设备', desc: '磁盘分区工具', example: 'fdisk -l /dev/sda' },
      { name: 'mount', syntax: 'mount [选项] 设备 挂载点', desc: '挂载文件系统', example: 'mount /dev/sdb1 /mnt/usb' },
      { name: 'umount', syntax: 'umount [选项] 挂载点', desc: '卸载文件系统', example: 'umount /mnt/usb' },
      { name: 'lsblk', syntax: 'lsblk [选项]', desc: '列出块设备信息', example: 'lsblk' },
      { name: 'blkid', syntax: 'blkid [设备]', desc: '显示块设备的 UUID 和文件系统类型', example: 'blkid /dev/sda1' },
    ]
  }
]

const filteredCategories = computed(() => {
  const keyword = search.value.trim().toLowerCase()
  if (!keyword) return categories
  return categories
    .map(cat => ({
      ...cat,
      commands: cat.commands.filter(
        cmd =>
          cmd.name.toLowerCase().includes(keyword) ||
          cmd.desc.toLowerCase().includes(keyword) ||
          cmd.syntax.toLowerCase().includes(keyword) ||
          cmd.example.toLowerCase().includes(keyword)
      )
    }))
    .filter(cat => cat.commands.length > 0)
})
</script>

<style scoped>
.linux-commands {
  min-height: 100vh;
  background: linear-gradient(135deg, #0c0c1d 0%, #1a1a3e 50%, #0c0c1d 100%);
  padding: 20px;
  color: #fff;
}

h2 {
  color: #fff;
}

.search-input {
  margin-bottom: 20px;
}

:deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

:deep(.el-input__inner:focus) {
  border-color: #409eff;
}

:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__header) {
  background: rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
}

:deep(.el-collapse-item__wrap) {
  background: transparent;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-collapse-item__content) {
  background: transparent;
  color: #fff;
}

:deep(.el-table) {
  background: transparent;
  color: #fff;
}

:deep(.el-table th) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table tr) {
  background: transparent;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(255, 255, 255, 0.03);
}

:deep(.el-table__empty-text) {
  color: rgba(255, 255, 255, 0.5);
}
</style>
