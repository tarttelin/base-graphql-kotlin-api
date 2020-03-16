
const siteConfig = {
  title: 'Phone service',
  tagline: 'GraphQL api for MyO2 Business',
  url: 'https://fubar.github.io',
  baseUrl: '/phone-service/',
  projectName: 'phone-service',
  organizationName: 'Fubar Industries',
  headerLinks: [
    { search: false },
    { doc: 'getting-started', label: 'Docs' },
    { href: 'https://github.com/Fubar/phone', label: 'GitHub' }
  ],
  colors: {
    primaryColor: '#000099',
    secondaryColor: '#01325A'
  },
  copyright: `Copyright © ${new Date().getFullYear()} Fubar Industries.`,
  highlight: {
    theme: 'github'
  },
  onPageNav: 'separate',
  cleanUrl: true,
  docsSideNaveCollapsible: true,
  repoUrl: 'https://github.com/Fubar/phone',
  editUrl: 'https://github.com/Fubar/phone/tree/master/docs/',
  scripts: [
    'https://cdnjs.cloudflare.com/ajax/libs/mermaid/8.4.4/mermaid.min.js',
    '/init.js',
  ],
  markdownPlugins: [
    (md) => {
      md.renderer.rules.fence_custom.mermaid = (tokens, idx, options, env, instance) => {
        return `<div class="mermaid">${tokens[idx].content}</div>`;
      };
    }
  ]
};

module.exports = siteConfig;
