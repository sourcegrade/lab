import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    '../../common/ui/components/**/*.{js,ts,jsx,tsx,mdx}',
    '../../common/ui/lib/**/*.{js,ts,jsx,tsx,mdx}',
    '../../common/ui/models/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic':
          'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
      },
    },
  },
  plugins: [],
}
export default config
