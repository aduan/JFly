package net.jfly.config;

/**
 * �����߳�������
 */
public class Constants {
	// �����Ϊ��������.
	// 1:��ܿ�������
	private boolean devMode = false;

	/**
	 *���ÿ���ģʽ�������,�����ڿ��������ж���Ŀ������Ϣ�Ĵ���
	 */
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public boolean isDevMode() {
		return devMode;
	}

}
